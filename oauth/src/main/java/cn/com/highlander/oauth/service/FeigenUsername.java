package cn.com.highlander.oauth.service;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Alemand
 * @since 2019/9/5
 */
//@FeignClient("aquaman-admin")
public interface FeigenUsername {

    //@RequestMapping("/username")
    String getResponse(@RequestParam(value = "username") String username);
}
