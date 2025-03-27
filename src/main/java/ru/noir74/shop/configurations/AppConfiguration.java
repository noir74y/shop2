package ru.noir74.shop.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.noir74.shop.misc.ItemSorting;

@Configuration
@Getter
@PropertySource(value = "classpath:application.properties")
public class AppConfiguration {

    @Value("${product.default.page.number}")
    private Integer DefaultPageNumber;

    @Value("${product.default.page.size}")
    private Integer DefaultPageSize;

    @Value("${product.default.page.sorting}")
    private ItemSorting DefaultPageSorting;
}