
package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.Image;
import com.yashcode.EcommerceBackend.entity.dto.ImageDto;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.image.IImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageControllerTests {

    @Mock
    private IImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveImages() throws Exception {
        List<MultipartFile> files = new ArrayList<>();
        List<ImageDto> imageDtos = new ArrayList<>();
        when(imageService.saveImages(anyLong(), any())).thenReturn(imageDtos);

        ResponseEntity<ApiResponse> response = imageController.saveImages(files, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Upload successful", response.getBody().getMessage());
    }

    @Test
    void testDownloadImage() throws SQLException {
        Image image = new Image();
        image.setId(1L);
        image.setFileType("image/png");
        image.setFileName("test.png");
        when(imageService.getImageById(anyLong())).thenReturn(image);

        ResponseEntity<Resource> response = imageController.downloadImage(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(MediaType.parseMediaType("image/png"), response.getHeaders().getContentType());
        assertEquals("attachments; filename=\"test.png\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    void testUpdateImage() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(imageService.getImageById(anyLong())).thenReturn(null);

        ResponseEntity<ApiResponse> response = imageController.updateImage(1L, file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Update success!", response.getBody().getMessage());
    }

    @Test
    void testDeleteImage() throws Exception {
        when(imageService.getImageById(anyLong())).thenReturn(null);

        ResponseEntity<ApiResponse> response = imageController.deleteImage(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("delete success!", response.getBody().getMessage());
    }
}
