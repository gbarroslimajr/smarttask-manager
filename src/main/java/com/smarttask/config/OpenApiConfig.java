package com.smarttask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartTaskManagerOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SmartTaskManager API")
                .description("API REST para gerenciamento de tarefas, projetos e usuários")
                .version("v1")
                .contact(new Contact()
                    .name("SmartTaskManager Team")
                    .email("support@smarttask.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento")
            ))
            .tags(List.of(
                new Tag().name("Users").description("Operações relacionadas a usuários"),
                new Tag().name("Tasks").description("Operações relacionadas a tarefas"),
                new Tag().name("Projects").description("Operações relacionadas a projetos")
            ));
    }
}
