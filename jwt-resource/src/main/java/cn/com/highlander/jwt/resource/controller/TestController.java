package cn.com.highlander.jwt.resource.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.highlander.common.util.R;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Alemand
 * @since 2019/9/6
 */
@RestController
public class TestController {


    @RequestMapping("/hello")
    public R<String> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return new R<>(currentPrincipalName);
    }
}
