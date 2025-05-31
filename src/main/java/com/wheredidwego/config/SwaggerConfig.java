package com.wheredidwego.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// URI : http://localhost:8080/swagger-ui/index.html
@OpenAPIDefinition(
        info = @Info(title = "우리 어디 갔었더라 API 문서", version = "v1", description = "지도 기반 여행 기록 서비스")
)
@Configuration
public class SwaggerConfig {
}

