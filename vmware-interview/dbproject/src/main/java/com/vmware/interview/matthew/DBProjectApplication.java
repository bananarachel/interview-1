/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;

@SpringBootApplication
@EnableSwagger2
public class DBProjectApplication
{

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2).groupName("DBProject")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vmware.interview.matthew"))
                .paths(any()).build().apiInfo(new ApiInfo("DBProject Services",
                        "A set of services to provide data access to customer and service", "1.0.0", null,
                        new Contact("Matthew Jiang", "", null), null, null));
    }

    public static void main(String[] args)
    {
        SpringApplication.run(DBProjectApplication.class, args);
    }

}
