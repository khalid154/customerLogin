package com.customer.login.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

	

	@Value("${swagger.host}")
	private String host;
	
	@Bean
	public Docket api() {
		Docket docket= new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(metaData())
				.securitySchemes(Arrays.asList(apiKey()))
				.useDefaultResponseMessages(false);
		docket.host(host);
		return docket;
	}
	
	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
		return springfox.documentation.spi.service.contexts.SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		springfox.documentation.service.AuthorizationScope authorizationScope
		= new springfox.documentation.service.AuthorizationScope("global", "accessEverything");
		springfox.documentation.service.AuthorizationScope[] authorizationScopes = new springfox.documentation.service.AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(
				new SecurityReference("JWT", authorizationScopes));
	}
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Cravex Login Service")
				.description("Cravex Login Service REST API Description")
				.termsOfServiceUrl("urn:tos")
				.contact(getContact())
				.license("Licence Url")
				.version("1.0")
				.build();
	}
	private Contact getContact() {
		return new Contact(
	            "Contact Name", "http://www.factoryos.in", "operations@factoryos.in");
	}
	private HashSet<String> produceConsume() {
		return new HashSet<String>(Arrays.asList("application/json","application/xml"));
	}

}
