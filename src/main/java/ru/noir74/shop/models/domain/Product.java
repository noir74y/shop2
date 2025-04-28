package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.noir74.shop.models.generic.GenericDomain;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends GenericDomain {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    private MultipartFile file;
}
