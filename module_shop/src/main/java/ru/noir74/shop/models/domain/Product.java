package ru.noir74.shop.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    @JsonIgnore // не кэшировать. Image кэшируется в своем сервисе
    private FilePart file;

    public Integer getPrice() {
        return Optional.ofNullable(price).orElse(0);
    }

    static public boolean isFileReadyToBeSaved(FilePart fileToCheck) {
        return Objects.nonNull(fileToCheck) && fileToCheck.filename().matches("^.+\\.\\w+$");
    }
}
