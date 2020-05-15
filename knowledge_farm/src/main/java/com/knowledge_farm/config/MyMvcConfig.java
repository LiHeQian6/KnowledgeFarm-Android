package com.knowledge_farm.config;

import com.knowledge_farm.component.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * @ClassName MyMvcConfig
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 15:47
 */
@Configuration
@PropertySource(value = {"classpath:photo.properties"})
public class MyMvcConfig {
    @Value("${file.staticMapping}")
    private String staticMapping;
    @Value("${file.photoLocation}")
    private String photoLocation;

    //所有的WebMvcConfigurerAdapter组件都会在一起起作用
    @Bean //将组件注册在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("init");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //静态资源：*.css,*.js
                //StringBoot已经做好了静态资源映射
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/adminaaaa/**")
                        .excludePathPatterns("/admin/toLogin", "/admin/changeTestCode", "/admin/login");
            }

            //磁盘资源映射
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(staticMapping).addResourceLocations("file:" + photoLocation + File.separator);
            }
        };
        return adapter;
    }

}
