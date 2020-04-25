package com.knowledge_farm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName Swagger2Config
 * @Description
 * @Author 张帅华
 * @Date 2020-04-24 17:50
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//用来创建该Api的基本信息
                .select()//select()函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现
                //为当前包路径,所有Controller所在的包路径
                .apis(RequestHandlerSelectors.basePackage("com.knowledge_farm"))
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
         //页面标题
        .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
         //创建人
        .contact(new Contact("Ricky", "http://www.bytebeats.com", "ricky_feng@163.com"))
         //版本号
        .version("1.0")
         //描述
        .description("API 描述")
        .build();
    }

}
