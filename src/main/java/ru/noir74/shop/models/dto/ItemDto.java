package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.generic.GenericDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto extends GenericDto {
    private Product product;
    private Integer quantity;
    private Integer price;
}
