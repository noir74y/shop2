package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    private FilePart file;

    static public boolean isFileReadyToBeSaved(FilePart fileToCheck) {
        return Objects.nonNull(fileToCheck) && fileToCheck.filename().matches("^.+\\.\\w+$");
    }
}
