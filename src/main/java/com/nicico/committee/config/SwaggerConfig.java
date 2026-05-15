package com.nicico.committee.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.security.oauth2.client.registration.oserver.client-id}")
    private String clientId;

    @Value(("${spring.security.oauth2.client.registration.oserver.client-secret}"))
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.oserver.swagger-token-uri}")
    private String accessTokenUri;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("companies-endpoints")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nicico.committee"))
                .apis(RequestHandlerSelectors.basePackage("com.nicico.committee.controller.blacklist").negate())
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema()))
                .apiInfo(getApiInfo());
    }

    @Bean
    public Docket blacklistApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("blacklist-endpoints")   // This appears in the dropdown
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nicico.committee.controller.blacklist"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema()))
                .apiInfo(getApiInfo());
    }

    private OAuth securitySchema() {
        List<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        authorizationScopeList.add(new AuthorizationScope("user_info", ""));

        List<GrantType> grantTypes = new ArrayList<>();
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
        grantTypes.add(passwordCredentialsGrant);

        return new OAuth("oauth2", authorizationScopeList, grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("user_info", "");

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration
                (clientId, clientSecret, "", "", "Bearer Access Token", ApiKeyVehicle.HEADER, HttpHeaders.AUTHORIZATION, "");
    }



    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "NICICO",
                "NICICO API for companies",
                "1.0",
                null,
                new Contact("Admin", "https://www.nicico.com/", "admin@nicico.com"),
                null,
                null,
                Collections.emptyList()
        );
    }

}
