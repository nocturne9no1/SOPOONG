package com.sopoong.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// http://localhost:8080/swagger-ui.html#!
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {
	private final TypeResolver typeResolver;
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("A404")
			.description("API")
			.version("1.0")
			.build();
	}
	
	private Set<String> getConsumeContentTypes() {
		Set<String> consumes = new HashSet<>();
		consumes.add("application/json;charset=UTF-8");
		consumes.add("application/x-www-form-urlencoded");
		return consumes;
	}
	
	private Set<String> getProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json;charset=UTF-8");
		return produces;
	}
	
	@Bean
	public Docket commonApi() {
		return new Docket(DocumentationType.SWAGGER_2).alternateTypeRules(
                AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(Page.class)))
				.consumes(getConsumeContentTypes())
				.produces(getProduceContentTypes())
				.apiInfo(apiInfo()).securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.newArrayList(authorizationKey(), httpRequestKey()))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/**"))
				.build();
	}
	private ApiKey authorizationKey() {
		return new ApiKey("JWT_TOKEN", "X-AUTH-TOKEN", "header");
	}
	private ApiKey httpRequestKey() {
		return new ApiKey("HTTP_REQUEST","sopoong", "header");
	}
	
	/**
	 * securityContext
	 * 
	 * @return
	 */
	private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
		
		return springfox.documentation.spi.service.contexts.SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.any())
				.build();
	}
	
	/**
	 * defaultAuth
	 * 
	 * @return
	 */
	private List<SecurityReference> defaultAuth() {

		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		
		return Lists.newArrayList(new SecurityReference("JWT_TOKEN", authorizationScopes),
				new SecurityReference("HTTP_REQUEST", authorizationScopes));
	}
	
	@Getter @Setter
    @ApiModel
    static class Page {
        @ApiModelProperty(value = "페이지 번호(0..N)")
        private Integer page;

        @ApiModelProperty(value = "페이지 크기", allowableValues="range[0, 100]")
        private Integer size;

        @ApiModelProperty(value = "정렬(사용법: 컬럼명,ASC|DESC)")
        private List<String> sort;
    }
}
