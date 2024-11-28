package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.entity.dto.ProductDto;
import com.yashcode.EcommerceBackend.entity.dto.ProductUpdateDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
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
        productController=new ProductController(productService);
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
    public void testGetProductsByBrandName_Success() {
        // Arrange
        String brandName = "Samsung";
        List<Product> products = new ArrayList<>();
        products.add(new Product("Galaxy S21", "Samsung", new BigDecimal(799.99)));
        products.add(new Product("Galaxy Note 20", "Samsung", new BigDecimal(999.99)));

        List<ProductDto> convertedProducts = new ArrayList<>();
        convertedProducts.add(new ProductDto("Galaxy S21", "Samsung", new BigDecimal(799.99)));
        convertedProducts.add(new ProductDto("Galaxy Note 20", "Samsung", new BigDecimal(999.99)));

        when(productService.getProductsByBrand(brandName)).thenReturn(products);
        when(productService.getConvertedProducts(products)).thenReturn(convertedProducts);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandName(brandName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(convertedProducts.size(), ((List<ProductDto>) response.getBody().getData()).size());
        verify(productService, times(1)).getProductsByBrand(brandName);
        verify(productService, times(1)).getConvertedProducts(products);
    }

    @Test
    public void testGetProductsByBrandName_NoProductsFound() {
        // Arrange
        String brandName = "NonExistingBrand";
        List<Product> products = new ArrayList<>();

        when(productService.getProductsByBrand(brandName)).thenReturn(products);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandName(brandName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No products with given brand name", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).getProductsByBrand(brandName);
    }

    @Test
    public void testGetProductsByBrandName_ResourceNotFoundException() {
        // Arrange
        String brandName = "NonExistingBrand";

        when(productService.getProductsByBrand(brandName))
                .thenThrow(new ResourceNotFoundException("Brand not found"));

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandName(brandName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Brand not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).getProductsByBrand(brandName);
    }


    @Test
    void testGetProductsByName_Success() {
        // Arrange
        String productName = "Sample Product";
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);

        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(new ProductDto());

        when(productService.getProductsByName(productName)).thenReturn(products);
        when(productService.getConvertedProducts(products)).thenReturn(productDtos);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByName(productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof List);
    }

    @Test
    void testGetProductsByName_NoProductsFound() {
        // Arrange
        String productName = "Non-Existing Product";
        List<Product> products = new ArrayList<>();

        when(productService.getProductsByName(productName)).thenReturn(products);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByName(productName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No products with given name", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetProductsByName_ExceptionHandling() {
        // Arrange
        String productName = "Sample Product";
        when(productService.getProductsByName(productName)).thenThrow(new ResourceNotFoundException("Error retrieving products"));

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByName(productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // It returns a 200 OK even in case of exception
        assertEquals("Error retrieving products", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
    @Test
    public void testSortProducts_Success() {
        // Arrange
        List<Product> sortedProducts = new ArrayList<>();
        sortedProducts.add(new Product("Product A", "Brand A",new BigDecimal(1)));
        sortedProducts.add(new Product("Product B", "Brand B", new BigDecimal(1)));
        when(productService.sortByField(anyString())).thenReturn(sortedProducts);

        // Act
        List<Product> response = productController.sortProducts("name");

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Product A", response.get(0).getName());
        verify(productService, times(1)).sortByField("name");
    }

    @Test
    public void testSortProductsByDesc_Success() {
        // Arrange
        List<Product> sortedProductsDesc = new ArrayList<>();
        sortedProductsDesc.add(new Product("Product B", "Brand B", new BigDecimal(1)));
        sortedProductsDesc.add(new Product("Product A", "Brand A", new BigDecimal(1)));
        when(productService.sortByFieldDesc(anyString())).thenReturn(sortedProductsDesc);

        // Act
        List<Product> response = productController.sortProductsByDesc("name");

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Product B", response.get(0).getName());
        verify(productService, times(1)).sortByFieldDesc("name");
    }

    @Test
    public void testProductPagination_Success() {
        // Arrange
        List<Product> paginatedProducts = new ArrayList<>();
        paginatedProducts.add(new Product("Product A", "Brand A", new BigDecimal(1)));
        paginatedProducts.add(new Product("Product B", "Brand B", new BigDecimal(1)));
        Page<Product> productPage = mock(Page.class);
        when(productPage.getContent()).thenReturn(paginatedProducts);
        when(productService.getProductByPagination(1, 1)).thenReturn(productPage);

        // Act
        List<Product> response = productController.productPagination(1, 1);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Product A", response.get(0).getName());
        verify(productService, times(1)).getProductByPagination(1, 1);
    }

    @Test
    public void testProductPaginationAndSorting_Success() {
        // Arrange
        List<Product> paginatedSortedProducts = new ArrayList<>();
        paginatedSortedProducts.add(new Product("Product A", "Brand A", new BigDecimal(1)));
        paginatedSortedProducts.add(new Product("Product B", "Brand B", new BigDecimal(1)));
        Page<Product> productPage = mock(Page.class);
        when(productPage.getContent()).thenReturn(paginatedSortedProducts);
        when(productService.getProductByPaginationAndSorting(anyInt(), anyInt(), anyString()))
                .thenReturn(productPage);

        // Act
        List<Product> response = productController.productPaginationAndSorting(0, 2, "name");

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Product A", response.get(0).getName());
        assertEquals("Product B", response.get(1).getName());
        verify(productService, times(1)).getProductByPaginationAndSorting(0, 2, "name");
    }
    @Test
    public void testGetProductsByCategoryAndBrand_Success() {
        // Arrange
        String category = "Electronics";
        String brandName = "Samsung";

        List<Product> products = new ArrayList<>();
        products.add(new Product("Smartphone", "Samsung", new BigDecimal(1000)));
        products.add(new Product("TV", "Samsung", new BigDecimal(1500)));

        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(new ProductDto("Smartphone", "Samsung", new BigDecimal(1000)));
        productDtos.add(new ProductDto("TV", "Samsung", new BigDecimal(1500)));

        when(productService.getProductsByCategoryAndBrand(category, brandName)).thenReturn(products);
        when(productService.getConvertedProducts(products)).thenReturn(productDtos);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByCategoryAndBrand(category, brandName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(2, ((List<?>) response.getBody().getData()).size());
        verify(productService, times(1)).getProductsByCategoryAndBrand(category, brandName);
        verify(productService, times(1)).getConvertedProducts(products);
    }

    @Test
    public void testGetProductsByCategoryAndBrand_NoProductsFound() {
        // Arrange
        String category = "Fashion";
        String brandName = "Nike";

        when(productService.getProductsByCategoryAndBrand(category, brandName)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByCategoryAndBrand(category, brandName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No products with given category name and brand name", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).getProductsByCategoryAndBrand(category, brandName);
        verify(productService, times(0)).getConvertedProducts(anyList());
    }

    @Test
    public void testGetProductsByCategoryAndBrand_ResourceNotFoundException() {
        // Arrange
        String category = "InvalidCategory";
        String brandName = "InvalidBrand";

        when(productService.getProductsByCategoryAndBrand(category, brandName))
                .thenThrow(new ResourceNotFoundException("Category or brand not found"));

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByCategoryAndBrand(category, brandName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category or brand not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).getProductsByCategoryAndBrand(category, brandName);
        verify(productService, times(0)).getConvertedProducts(anyList());
    }

    @Test
    public void testCountProductByBrandNameAndName_Success() {
        // Arrange
        String brand = "Samsung";
        String name = "Smartphone";
        long productCount = 5;

        when(productService.countProductsByBrandAndName(brand, name)).thenReturn((long) productCount);

        // Act
        ResponseEntity<ApiResponse> response = productController.countProductByBrandNameAndName(brand, name);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Count@", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(productCount, response.getBody().getData());
        verify(productService, times(1)).countProductsByBrandAndName(brand, name);
    }

    @Test
    public void testCountProductByBrandNameAndName_ResourceNotFoundException() {
        // Arrange
        String brand = "NonExistingBrand";
        String name = "NonExistingName";

        when(productService.countProductsByBrandAndName(brand, name))
                .thenThrow(new ResourceNotFoundException("Brand or product name not found"));

        // Act
        ResponseEntity<ApiResponse> response = productController.countProductByBrandNameAndName(brand, name);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Brand or product name not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).countProductsByBrandAndName(brand, name);
    }

    @Test
    public void testGetProductsByBrandAndName_Success() {
        // Arrange
        String brandName = "Samsung";
        String productName = "Galaxy S21";

        List<Product> products = new ArrayList<>();
        products.add(new Product("Galaxy S21", "Samsung", new BigDecimal(799.99)));
        products.add(new Product("Galaxy Note 20", "Samsung", new BigDecimal(999.99)));

        List<ProductDto> convertedProducts = new ArrayList<>();
        convertedProducts.add(new ProductDto("Galaxy S21", "Samsung", new BigDecimal(799.99)));
        convertedProducts.add(new ProductDto("Galaxy Note 20", "Samsung", new BigDecimal(999.99)));

        when(productService.getProductsByBrandAndName(brandName, productName)).thenReturn(products);
        when(productService.getConvertedProducts(products)).thenReturn(convertedProducts);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandAndName(brandName, productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(convertedProducts.size(), ((List<ProductDto>) response.getBody().getData()).size());
        verify(productService, times(1)).getProductsByBrandAndName(brandName, productName);
        verify(productService, times(1)).getConvertedProducts(products);
    }

    @Test
    public void testGetProductsByBrandAndName_NoProductsFound() {
        // Arrange
        String brandName = "Samsung";
        String productName = "NonExistingProduct";
        List<Product> products = new ArrayList<>();

        when(productService.getProductsByBrandAndName(brandName, productName)).thenReturn(products);

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandAndName(brandName, productName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No products with this brand name and product name", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).getProductsByBrandAndName(brandName, productName);
    }

    @Test
    public void testGetProductsByBrandAndName_ResourceNotFoundException() {
        // Arrange
        String brandName = "Samsung";
        String productName = "NonExistingProduct";

        when(productService.getProductsByBrandAndName(brandName, productName))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        // Act
        ResponseEntity<ApiResponse> response = productController.getProductsByBrandAndName(brandName, productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(productService, times(1)).getProductsByBrandAndName(brandName, productName);
    }



}
