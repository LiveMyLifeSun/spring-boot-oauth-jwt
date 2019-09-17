package cn.com.highlander.jwt.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Alemand
 * @since 2019/8/29
 */
@Component
public class AquamanUserDetailsService  implements UserDetailsService {

    //这里需要mapper
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 这个地方可以通过username从数据库获取正确的用户信息，包括密码和权限等。
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        //grantedAuthorityList.add(new MyGrantedAuthority("MY_ROLE1", "MY_MENU1"));
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(username, "$2a$10$WIDPnAxDUfQq/asUAnMkROiAHUEvkJ/eI9TWlyCoa6QMW2YJUkNEW", grantedAuthorityList);
    }

}
