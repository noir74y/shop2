package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.noir74.shop.services.ImageService;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
//    private final ImageRepository imageRepository;

//    @Override
//    @Transactional(readOnly = true)
//    public Image findImageById(Long id) {
//        return imageMapper.entity2domain(imageRepository.findById(id).orElseThrow(() -> new NotFoundException("image is not found", "id=" + id)));
//    }
//
//    @Override
//    @Transactional
//    public void setImage(Image image) {
//        imageRepository.findById(image.getProductId()).ifPresentOrElse(entity -> {
//                    entity.setImage(image.getImage());
//                    entity.setImageName(image.getImageName());
//                },
//                () -> imageRepository.save(imageMapper.domain2entity(image)));
//    }
//
//    @Override
//    @Transactional
//    public void deleteById(Long id) {
//        imageRepository.deleteById(id);
//    }
}
