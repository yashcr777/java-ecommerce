package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.dto.ProductDto;
import com.yashcode.EcommerceBackend.dto.ProductUpdateDTO;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.product.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductControllerTests {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllProducts_Success() {
        // Arrange
        List<Product> productList = new ArrayList<>();
        productList.add(new Product()); // Add sample product
        when(productService.getAllProducts()).thenReturn(productList);
        when(productService.getConvertedProducts(anyList())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found All Products", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testAddProduct_Success() {
        // Arrange
        AddProductDTO addProductDTO = new AddProductDTO();
        Product product = new Product();
        when(productService.addProduct(any(AddProductDTO.class))).thenReturn(product);
        when(productService.convertToDo(any(Product.class))).thenReturn(new ProductDto());

        // Act
        ResponseEntity<ApiResponse> response = productController.addProduct(addProductDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testGetProductById_Success() {
        // Arrange
        Product product = new Product();
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(productService.convertToDo(any(Product.class))).thenReturn(new ProductDto());

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testUpdateProduct_Success() {
        // Arrange
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        Product product = new Product();
        when(productService.updateProduct(any(ProductUpdateDTO.class), anyLong())).thenReturn(product);
        when(productService.convertToDo(any(Product.class))).thenReturn(new ProductDto());

        // Act
        ResponseEntity<ApiResponse> response = productController.updateProduct(updateDTO, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testDeleteProduct_Success() {
        // Act
        ResponseEntity<ApiResponse> response = productController.deleteProduct(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getMessage());
    }

    @Test
    public void testGetProductByBrandAndName_Success() {
        // Arrange
        List<Product> products = new ArrayList<>();
        when(productService.getProductsByBrandAndName(anyString(), anyString())).thenReturn(products);
        when(productService.getConvertedProducts(anyList())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandAndName("BrandA", "ProductA");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testGetProductsByName_NotFound() {
        // Arrange
        when(productService.getProductsByName(anyString())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByName("NonExistingProduct");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No products with given name", response.getBody().getMessage());
    }
}
