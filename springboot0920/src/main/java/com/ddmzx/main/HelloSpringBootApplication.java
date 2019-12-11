package com.ddmzx.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * 暂时关闭数据源:
 @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
 */
/*@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //不关启动会报错
@RestController*/
public class HelloSpringBootApplication {

    /*
    1、SpringBoot的启动类  @SpringBootApplication
       SpringBoot就应该运行这个类的main方法来启动SpringBoot应用
    2、@RestController代表的就是一个控制器   等同于@Controller+@ResponseBody
        以Json的方式返回数据
     */
    @RequestMapping("/hello")//代表请求
    public  String  sayHello(){

        return  "Hello  Springboot";
    }
/*    public static void main(String[] args) {
        SpringApplication.run(HelloSpringBootApplication.class,args);
    }*/
}
