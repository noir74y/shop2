package ru.noir74.shop;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.OrderItem;
import ru.noir74.shop.repositories.ImageRepository;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.OrderItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.ItemService;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@SpringBootTest
class ShopApplicationTests {
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	ItemService itemService;
	@Autowired
	OrderService orderService;
	@Autowired
	ImageService imageService;
	@Autowired
	CartService cartService;

	@BeforeEach
    void setUp() {
		orderItemRepository.deleteAll();
		orderRepository.deleteAll();
		imageRepository.deleteAll();
		itemRepository.deleteAll();
	}


	@Test
	void contextLoads() {
		var itemId = itemService.create(Item.builder().title("product1").price(1).description("desc").build());
		var orderId = orderService.create(List.of(OrderItem.builder().itemId(itemId).quantity(15).build()));
		//orderItemRepository.saveAll();
	}
}
