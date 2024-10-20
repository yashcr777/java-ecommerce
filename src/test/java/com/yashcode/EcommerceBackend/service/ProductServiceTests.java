package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.CategoryRepository;
import com.yashcode.EcommerceBackend.Repository.ImageRepository;
import com.yashcode.EcommerceBackend.Repository.ProductRepository;
import com.yashcode.EcommerceBackend.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.dto.ProductUpdateDTO;
import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ProductNotFoundException;
import com.yashcode.EcommerceBackend.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private AddProductDTO addProductDTO;
    private ProductUpdateDTO productUpdateDTO;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics");
        product = new Product("iPhone", "Apple", BigDecimal.valueOf(1000.0) , 50, "Smartphone", List.of(category));

        Category categoryDto = new Category();
        categoryDto.setName("Electronics");

        addProductDTO = new AddProductDTO();
        addProductDTO.setName("iPhone");
        addProductDTO.setBrand("Apple");
        addProductDTO.setPrice(BigDecimal.valueOf(1000.0));
        addProductDTO.setInventory(50);
        addProductDTO.setDescription("Smartphone");
        addProductDTO.setCategory(categoryDto);

        productUpdateDTO = new ProductUpdateDTO();
        productUpdateDTO.setName("iPhone Pro");
        productUpdateDTO.setBrand("Apple");
        productUpdateDTO.setPrice(BigDecimal.valueOf(1000.0));
        productUpdateDTO.setInventory(40);
        productUpdateDTO.setDescription("Pro version of iPhone");
        productUpdateDTO.setCategory(category);
    }

    @Test
    void testAddProduct_Success() {
        when(productRepository.existsByNameAndBrand(addProductDTO.getName(), addProductDTO.getBrand())).thenReturn(false);
        when(categoryRepository.findByName(addProductDTO.getCategory().getName())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.addProduct(addProductDTO);

        assertEquals("iPhone", createdProduct.getName());
        assertEquals("Apple", createdProduct.getBrand());
    }

    @Test
    void testAddProduct_AlreadyExists() {
        when(productRepository.existsByNameAndBrand(addProductDTO.getName(), addProductDTO.getBrand())).thenReturn(true);
        assertThrows(AlreadyExistException.class, () -> productService.addProduct(addProductDTO));
    }

    @Test
    void testGetProductById_ProductFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product foundProduct = productService.getProductById(1L);
        assertEquals(product, foundProduct);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testDeleteProductById_ProductFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);
        productService.deleteProductById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(1L));
    }

    @Test
    void testUpdateProduct_ProductFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName(productUpdateDTO.getCategory().getName())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(productUpdateDTO, 1L);
        assertEquals("iPhone Pro", updatedProduct.getName());
        assertEquals(1200.0, updatedProduct.getPrice());
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productUpdateDTO, 1L));
    }

    @Test
    void testGetAllProducts_Success() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testGetProductByCategory_Success() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByCategoryName("Electronics")).thenReturn(products);

        List<Product> result = productService.getProductByCategory("Electronics");
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testSortByField() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(products);

        List<Product> result = productService.sortByField("name");
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testGetProductByPagination() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(PageRequest.of(0, 10))).thenReturn(productPage);

        Page<Product> result = productService.getProductByPagination(0, 10);
        assertEquals(productPage, result);
    }
}
