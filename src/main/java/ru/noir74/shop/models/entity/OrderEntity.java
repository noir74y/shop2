package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.noir74.shop.models.generic.GenericEntity;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orders", schema = "store")
public class OrderEntity extends GenericEntity {
    @Id
    private Long id;
    private Integer total;
}