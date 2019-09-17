package cn.com.highlander.jwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Alemand
 * @since 2019/8/29
 */
@RestController
public class TestController {
   /* @Autowired
    private FeigenUsername feigenUsername;*/
    @RequestMapping("/hello")
    public String sayHello(){
        return "aquaman";
        //return feigenUsername.getResponse("aquaman");
    }
}
