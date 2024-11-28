package com.yashcode.EcommerceBackend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yashcode.EcommerceBackend.Repository.ImageRepository;
import com.yashcode.EcommerceBackend.entity.Image;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.entity.dto.ImageDto;
import com.yashcode.EcommerceBackend.service.image.ImageService;
import com.yashcode.EcommerceBackend.service.product.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@SpringBootTest
class ImageServiceTests {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private IProductService iProductService;

    @InjectMocks
    private ImageService imageService;



    private Image i;

    @BeforeEach
    void setUp() {
        i=new Image();
        i.setId(1L);
        i.setFileName("File1");
    }

    @Test
    void testGetImageById() {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        Image result = imageService.getImageById(imageId);
        assertEquals(imageId, result.getId());
    }

    @Test
    void testDeleteImageById() {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        doNothing().when(imageRepository).delete(image);

        assertDoesNotThrow(() -> imageService.deleteImageById(imageId));
    }

    @Test
    void testSaveImages() throws IOException, SQLException {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[]{});

        when(iProductService.getProductById(productId)).thenReturn(product);

        Image image = new Image();
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        List<ImageDto> result = imageService.saveImages(productId, Arrays.asList(file));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateImage() throws IOException, SQLException {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("updated.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[]{});

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        assertDoesNotThrow(() -> imageService.updateImage(file, imageId));
    }
}
