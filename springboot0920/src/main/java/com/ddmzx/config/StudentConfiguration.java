package com.ddmzx.config;

import com.ddmzx.entity.Student;
import org.springframework.context.annotation.Bean;

/**
 * 将student注入到IOC容器中
 */
public class StudentConfiguration {

     @Bean
     public  Student   student(){
         return  new Student();
     }
}
