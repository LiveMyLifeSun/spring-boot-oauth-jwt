package cn.com.highlander.jwt.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

import lombok.Getter;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Alemand
 * @since 2019/9/2
 */
public class AquamanUser extends User{
    /**
     *用户的ID
     * 只提供get方法
     */
    @Getter
    private Integer id;


    public AquamanUser(Integer id,String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
}
