package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Long id;
    private byte[] image;
    private String imageName;
    private Long itemId;

    public boolean isImageReadyToBeSaved() {
        return Objects.nonNull(image) &&
                image.length != 0 &&
                Objects.nonNull(imageName) &&
                imageName.matches("^.+\\.\\w+$");
    }

    public String getImageType() {
        return Arrays.stream(imageName.split("\\."))
                .reduce((first, second) -> second)
                .orElse("");
    }
}
