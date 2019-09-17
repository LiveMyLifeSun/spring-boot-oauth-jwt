package cn.com.highlander.oauth.resource.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.highlander.common.util.R;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Alemand
 * @since 2019/9/6
 */
@Slf4j
@RestController
public class TestController {


    @RequestMapping("/hello")
    public R<String> test() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Authentication authentication = context.getAuthentication();
            String currentPrincipalName = authentication.getName();
            log.info("{}",currentPrincipalName);
        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            log.info("{}","出现异常");
        }
        return new R<>("alemand");
    }
}
