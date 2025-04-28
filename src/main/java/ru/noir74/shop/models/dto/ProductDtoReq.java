package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.noir74.shop.models.generic.GenericDtoReq;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoReq extends GenericDtoReq {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    private MultipartFile file;
    private Integer quantity;
}
