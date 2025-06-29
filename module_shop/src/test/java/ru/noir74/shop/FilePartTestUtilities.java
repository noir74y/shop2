package ru.noir74.shop;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilePartTestUtilities implements FilePart {
    private final MockMultipartFile mockMultipartFile;
    private final Resource resource;

    public FilePartTestUtilities(String filename) throws IOException {
        resource = new ClassPathResource(filename);
        InputStream inputStream = resource.getInputStream();
        this.mockMultipartFile = new MockMultipartFile(
                "file",
                filename,
                String.valueOf(MediaType.IMAGE_JPEG),
                inputStream
        );
    }

    @Override
    public String filename() {
        return mockMultipartFile.getOriginalFilename();
    }

    @Override
    public Mono<Void> transferTo(Path destination) {
        try {
            Files.copy(resource.getInputStream(), destination);
            return Mono.empty();
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    @Override
    public String name() {
        return mockMultipartFile.getName();
    }

    @Override
    public HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData(name(), filename());
        headers.setContentType(MediaType.parseMediaType(mockMultipartFile.getContentType()));
        return headers;
    }

    @Override
    public Flux<DataBuffer> content() {
        DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
        DataBuffer buffer = null;
        try {
            buffer = factory.wrap(mockMultipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Flux.just(buffer);
    }
}
