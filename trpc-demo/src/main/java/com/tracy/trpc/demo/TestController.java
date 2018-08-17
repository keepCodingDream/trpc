package com.tracy.trpc.demo;

import com.tracy.trpc.common.demo.Demo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tracy.
 * @create 2018-08-16 20:06
 **/
@RestController
public class TestController {

    @Resource
    private Demo demo;

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test(@RequestParam(value = "world") String world) {
        return demo.sayHello(world);
    }
}
