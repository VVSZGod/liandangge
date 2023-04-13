package com.jiamian.translation.config;

import java.util.List;

import com.jiamian.translation.common.config.SystemConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameterList = Lists.newArrayList();
        parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String")).parameterType("header")
                .required(false).build();
        parameterList.add(parameterBuilder.build());
        boolean isOpen = !SystemConfig.isPro();

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .globalOperationParameters(parameterList).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.jiamian.translation.controller"))
                .paths(PathSelectors.any()).build().enable(isOpen);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("com/jiamian/translation").version("1.0").build();
    }

}
