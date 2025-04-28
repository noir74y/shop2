package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.noir74.shop.models.generic.GenericDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends GenericDto {
    private Long id;
    private List<ItemDto> itemsDto;
    private Integer total;

    public Integer getOrderCost() {
        return itemsDto.stream().mapToInt(obj -> obj.getProduct().getPrice() * obj.getQuantity()).sum();
    }
}