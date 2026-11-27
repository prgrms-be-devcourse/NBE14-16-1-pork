package com.gridsandcircles.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Swagger(OpenAPI) 문서 설정.
 * 접속 주소: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Grids & Circles API")
				.description("Grids & Circles 커피 원두 주문 및 관리 서비스 API")
				.version("v1"));
	}
}
