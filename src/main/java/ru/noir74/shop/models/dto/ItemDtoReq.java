package ru.noir74.shop.models.dto;

import org.springframework.web.multipart.MultipartFile;

public class ItemDtoReq {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    private MultipartFile file;
}
