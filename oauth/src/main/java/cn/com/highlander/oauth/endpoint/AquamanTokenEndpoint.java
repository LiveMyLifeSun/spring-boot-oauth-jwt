package cn.com.highlander.oauth.endpoint;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.highlander.common.constant.CommonConstants;
import cn.com.highlander.common.constant.SecurityConstants;
import cn.com.highlander.common.util.R;
import cn.com.highlander.oauth.service.AquamanUser;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 管理token
 * </p>
 *
 * @author Alemand
 * @since 2019/9/2
 */
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class AquamanTokenEndpoint {
    /**
     * token的redis前缀
     */
    private static final String PROJECT_OAUTH_ACCESS = SecurityConstants.PROJECT_PREFIX + SecurityConstants.OAUTH_PREFIX + "access:";
    /**
     * 当前页
     */
    private static final String CURRENT = "current";
    /**
     * 每页显示数量
     */
    private static final String SIZE = "size";
    /**
     * 管理的token类
     */
    private final TokenStore tokenStore;
    /**
     * redis相关
     */
    private final RedisTemplate redisTemplate;

    /**
     * 退出登录
     *
     * @param authHeard 请求头
     * @return 响应体
     */
    @DeleteMapping("/logout")
    public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                     String authHeard) {
        //没有获取的token
        if (StrUtil.isBlank(authHeard)) {
            return R.<Boolean>builder()
                    .code(CommonConstants.SUCCESS)
                    .data(Boolean.FALSE)
                    .msg("退出失败，token为空")
                    .build();
        }
        //token的组成 Bearer ashdhasd
        String tokenValue = authHeard.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
            return R.<Boolean>builder()
                    .code(CommonConstants.SUCCESS)
                    .data(Boolean.FALSE)
                    .msg("退出失败，token无效")
                    .build();
        }
        //移除token
        tokenStore.removeAccessToken(accessToken);
        //获取刷新的token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        //移除刷新token
        tokenStore.removeRefreshToken(refreshToken);
        return R.<Boolean>builder()
                .code(CommonConstants.SUCCESS)
                .data(Boolean.TRUE)
                .build();
    }

    /**
     * 移除token
     *
     * @param token token
     * @return 响应体
     */
    @DeleteMapping("/{token}")
    public R<Boolean> removeToken(@PathVariable("token") String token) {
        return new R<Boolean>(redisTemplate.delete(PROJECT_OAUTH_ACCESS + token));
    }

    /**
     * @param params 请求参数
     * @return
     */
    @PostMapping("/page")
    public R getTokenPage(@RequestBody Map<String, Object> params) {
        List<Map<String, String>> list = new ArrayList<>();
        if (StringUtils.isEmpty(MapUtil.getInt(params, CURRENT)) || StringUtils.isEmpty(MapUtil.getInt(params, SIZE))) {
            params.put(CURRENT, 1);
            params.put(SIZE, 20);
        }
        //根据分页参数获取对应数据
        List<String> pages = findKeysForPage(PROJECT_OAUTH_ACCESS + "*", MapUtil.getInt(params, CURRENT), MapUtil.getInt(params, SIZE));

        for (String page : pages) {
            String accessToken = StrUtil.subAfter(page, PROJECT_OAUTH_ACCESS, true);
            OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
            Map<String, String> map = new HashMap<>(8);


            map.put(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
            map.put(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
            map.put(OAuth2AccessToken.EXPIRES_IN, token.getExpiresIn() + "");


            OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
            Authentication authentication = oAuth2Auth.getUserAuthentication();

            map.put(OAuth2Utils.CLIENT_ID, oAuth2Auth.getOAuth2Request().getClientId());
            map.put(OAuth2Utils.GRANT_TYPE, oAuth2Auth.getOAuth2Request().getGrantType());

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

                if (authenticationToken.getPrincipal() instanceof AquamanUser) {
                    AquamanUser user = (AquamanUser) authenticationToken.getPrincipal();
                    map.put("user_id", user.getId() + "");
                    map.put("username", user.getUsername() + "");
                }
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                //刷新token方式
                PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                if (authenticationToken.getPrincipal() instanceof AquamanUser) {
                    AquamanUser user = (AquamanUser) authenticationToken.getPrincipal();
                    map.put("user_id", user.getId() + "");
                    map.put("username", user.getUsername() + "");
                }
            }
            list.add(map);
        }
        Page result = new Page(MapUtil.getInt(params, CURRENT), MapUtil.getInt(params, SIZE));
        result.setRecords(list);
        result.setTotal(Long.valueOf(redisTemplate.keys(PROJECT_OAUTH_ACCESS + "*").size()));
        return new R(result);
    }

    private List<String> findKeysForPage(String patternKey, int pageNum, int pageSize) {
        ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        int tmpIndex = 0;
        int startIndex = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        assert cursor != null;
        while (cursor.hasNext()) {
            if (tmpIndex >= startIndex && tmpIndex < end) {
                result.add(cursor.next().toString());
                tmpIndex++;
                continue;
            }
            if (tmpIndex >= end) {
                break;
            }
            tmpIndex++;
            cursor.next();
        }
        return result;
    }
}
