package ru.noir74.shop.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.noir74.shop.misc.ItemSorting;

@Configuration
@Getter
public class AppConfiguration {

    @Value("${item.default.page.number}")
    private Integer DefaultPageNumber;

    @Value("${item.default.page.size}")
    private Integer DefaultPageSize;

    @Value("${item.default.page.sorting}")
    private ItemSorting DefaultPageSorting;
}