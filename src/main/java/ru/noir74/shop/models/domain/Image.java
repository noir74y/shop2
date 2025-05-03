package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Long id;
    private byte[] image;
    private String imageName;
    private Long productId;

    public String getImageType() {
        return Arrays.stream(imageName.split("\\."))
                .reduce((first, second) -> second)
                .orElse("");
    }
}
