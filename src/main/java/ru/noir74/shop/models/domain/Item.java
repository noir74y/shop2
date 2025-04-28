package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.noir74.shop.models.generic.GenericDomain;
import ru.noir74.shop.models.generic.GenericDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends GenericDomain {
    private Product product;
    private Integer quantity;
    private Integer price;
}
