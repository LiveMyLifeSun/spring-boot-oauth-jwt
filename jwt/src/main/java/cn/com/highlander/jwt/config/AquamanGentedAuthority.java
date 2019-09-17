package cn.com.highlander.jwt.config;

import org.springframework.security.core.GrantedAuthority;

/**
 * <p>
 * 用来配置用户的权限
 * </p>
 *
 * @author Alemand
 * @since 2019/8/29
 */
public class AquamanGentedAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        //这里返回的用户的角色
        return null;
    }
}
