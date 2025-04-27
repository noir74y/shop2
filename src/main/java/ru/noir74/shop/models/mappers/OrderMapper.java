package ru.noir74.shop.models.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.entity.OrderEntity;

import java.util.List;

import static reactor.core.publisher.Mono.justOrEmpty;

@Component
@RequiredArgsConstructor
public class OrderMapper {
//    private final ModelMapper modelMapper;
//    private final ItemMapper itemMapper;
//
//    @PostConstruct
//    private void setup() {
//        Converter<List<Item>, List<ItemEntity>> domains2entitiesConverter =
//                list -> itemMapper.bulkDomain2entity(list.getSource());
//        Converter<List<Item>, List<ItemDto>> domains2dtosConverter =
//                list -> itemMapper.bulkDomain2dto(list.getSource());
//        Converter<List<ItemEntity>, List<Item>> entities2domainsConverter =
//                list -> itemMapper.bulkEntity2domain(list.getSource());
//
//        TypeMap<Order, OrderEntity> domain2entityMapper = modelMapper.createTypeMap(Order.class, OrderEntity.class);
//        TypeMap<Order, OrderDto> domain2dtoMapper = modelMapper.createTypeMap(Order.class, OrderDto.class);
//        TypeMap<OrderEntity, Order> entity2domainMapper = modelMapper.createTypeMap(OrderEntity.class, Order.class);
//
//        domain2entityMapper.addMappings(modelMapper ->
//                modelMapper.using(domains2entitiesConverter)
//                        .map(Order::getItems, OrderEntity::setItemEntities));
//
//        domain2dtoMapper.addMappings(modelMapper ->
//                modelMapper.using(domains2dtosConverter)
//                        .map(Order::getItems, OrderDto::setItemsDto));
//
//        entity2domainMapper.addMappings(modelMapper ->
//                modelMapper.using(entities2domainsConverter)
//                        .map(OrderEntity::getItemEntities, Order::setItems));
//    }
//
//    public Mono<Order> dto2domain(Mono<OrderDto> mono) {
//        return mono.map(value -> modelMapper.map(value, Order.class));
//    }
//
//    public Mono<OrderDto> domain2dto(Mono<Order> mono) {
//        return mono.map(value -> modelMapper.map(value, OrderDto.class));
//    }
//
//    public Mono<OrderEntity> domain2entity(Mono<Order> mono) {
//        return mono.map(value -> modelMapper.map(value, OrderEntity.class));
//    }
//
//    public <I, O> Mono<O> in2out(Mono<I> input, Class<O> cls) {
//        return input.map(value -> modelMapper.map(value, cls));
//    }
//
//    public Mono<Order> entity2domain(Mono<OrderEntity> mono) {
//        return mono.map(value -> modelMapper.map(value, Order.class));
//    }
//
//    public Flux<Order> bulkEntity2domain(Iterable<OrderEntity> entities) {
//        //return Flux.fromStream(StreamSupport.stream(entities.spliterator(),true).map(this::entity2domain));
//    }
//
//    public List<OrderDto> bulkDomain2Dto(List<Order> domains) {
//        return domains.stream().map(this::domain2dto).toList();
//    }
}
