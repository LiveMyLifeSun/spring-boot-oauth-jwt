package cn.com.highlander.jwt.config;

import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

/**
 * <p>
 * 授权服务器相关配置
 * </p>
 *
 * @author Alemand
 * @since 2019/8/28
 */
//@Configuration
//@AllArgsConstructor
//@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
/*
    *//**
     * 数据源
     *//*
    private final DataSource dataSource;
    *//**
     * redis 连接
     *//*
    private final RedisConnectionFactory redisConnectionFactory;

    *//**
     * oauth2模式相关的东西
     *//*
    private final AuthenticationManager authenticationManagerBean;
    *//**
     * 加密方式
     *//*
    private final BCryptPasswordEncoder passwordEncoder;
    *//**
     * 用户相关东西
     *//*
    private final AquamanUserDetailsService userDetailsService;

    *//**
     * @param clients 用户的相关配置载体
     * @throws Exception
     *//*
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       *//* String secret = passwordEncoder.encode("secret");
        //配置客户端
        clients.
                //使用内存设置
                        inMemory()
                //设置clientID
                .withClient("client")
                //client_secret 要进行加密
                .secret(secret)
                //授权类型
                .authorizedGrantTypes("password", "refresh_token")
                //授权范围
                .scopes("read,writer");*//*
        clients.jdbc(dataSource);
        super.configure(clients);
    }


    *//**
     * 用来配置拦截的相关东西
     *
     * @param endpoints 请求的拦截点
     * @throws Exception
     *//*
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManagerBean)//使用密码模式
                .userDetailsService(userDetailsService) //不添加的话会出错
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)//设置允许的请求方式
                .tokenStore(tokenStore())//设置tokenStore存储内存中
        //.tokenEnhancer(tokenEnhancer())//设置token的增强器
                //.accessTokenConverter(jwtAccessTokenConverter())
        ;
        super.configure(endpoints);
    }

    *//**
     * 设置跨域相关的东西
     *//*
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    *//**
     * 设置tokenStore jwt的相关设置可在这里设置 默认是基于内存的 在这里设置为redis
     *
     * @return
     *//*
    @Bean
    public TokenStore tokenStore() {
       //设置redis的连接工厂
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        //设置redis的前缀
        tokenStore.setPrefix(SecurityConstants.PROJECT_PREFIX + SecurityConstants.OAUTH_PREFIX);
        //JwtTokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        return tokenStore;
    }


    //@Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("aquaman");
        return converter;
    }




    *//**
     * 令牌争抢器，比如添加，一个新的字段
     *
     * @return 返回一个增强后的令牌
     *//*
    public TokenEnhancer tokenEnhancer() {
        return (oAuth2AccessToken, oAuth2Authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("license", "aquaman");
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
            return oAuth2AccessToken;
        };
    }*/
}
