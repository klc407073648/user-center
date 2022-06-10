package com.klc.usercenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"dev","test"})
public class SwaggerConfig {

//配置Swagger的Bean实例
@Bean
public Docket swaggerSpringMvcPlugin() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .groupName("adminApi")
            .select()
            //只显示admin下面的路径
            .apis(RequestHandlerSelectors.basePackage("com.klc.usercenter.controller"))
            .paths(PathSelectors.any())
            .build();

}
//配置API的基本信息（会在http://项目实际地址/swagger-ui.html页面显示）
private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("用户中心")
        .description("用户中心接口文档描述")
        .termsOfServiceUrl("http://www.baidu.com")
        .contact(new Contact("yupi","https://github.com/klc407073648","12345678@qq.com"))
        .version("1.0")
        .build();
    }
}
