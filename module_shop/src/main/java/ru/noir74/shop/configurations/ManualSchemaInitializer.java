package ru.noir74.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ManualSchemaInitializer implements CommandLineRunner {
    private final DatabaseClient dbClient;

    @Override
    public void run(String... args) throws Exception {
        String schemaSql = StreamUtils.copyToString(
                new ClassPathResource("schema.sql").getInputStream(),
                StandardCharsets.UTF_8
        );
        dbClient.sql(schemaSql).then().block();
        System.out.println("Schema.sql executed manually");
    }
}