package ru.noir74.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.mappers.generic.GenericItemMapper;

@SpringBootApplication
public class ShopApplication implements CommandLineRunner {

    @Autowired
    private GenericItemMapper itemMapper;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ItemDto dto = new ItemDto();
        System.out.println("Mapping is about to start");
        var entity = itemMapper.domain2entity(itemMapper.dto2domain(dto));
        System.out.println("Mapping successful: " + entity);
    }
}