package ru.noir74.shop.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.services.ImageService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("{id}")
    public void getImage(@PathVariable("id") Long productId, HttpServletResponse response) throws IOException {
        Optional.ofNullable(imageService.findImageById(productId)).ifPresent(image -> {
            response.setContentType("image/" + image.getImageType());
            try {
                response.getOutputStream().write(image.getImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PostMapping("{id}")
    public String setImage(@PathVariable("id") Long productId, @RequestParam("file") MultipartFile file) throws IOException {
        imageService.setImageById(Image.builder().productId(productId).image(file.getBytes()).imageName(file.getOriginalFilename()).build());
        return "products";
    }
}
