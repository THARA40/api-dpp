package com.vanheurck.dpp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Digital Product Passport API",
                version = "v1",
                description = "REST API for managing Digital Product Passports (DPP)",
                contact = @Contact(
                        name = "Kushan",
                        email = "kushan@zeropoint.hr"
                ),
                license = @License(
                        name = "Zeropoint",
                        url = "https://www.zeropoint.hr"
                )
        )
)
public class OpenApiConfig {
    // no code needed, the annotations are enough
}
