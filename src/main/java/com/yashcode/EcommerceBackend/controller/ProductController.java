package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.dto.ProductDto;
import com.yashcode.EcommerceBackend.dto.ProductUpdateDTO;
import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.product.IProductService;
import com.yashcode.EcommerceBackend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse>getAllProducts(){
        try {
            List<Product>productList=productService.getAllProducts();
//            List<ProductDto>convertedProducts=productService.getConvertedProducts(productList);
            return ResponseEntity.ok(new ApiResponse("Found All Products",productList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",null));
        }
    }
    @PreAuthorize("hasRole('ROlE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>addProduct(@RequestBody AddProductDTO addProductDTO)
    {
        try {
            Product product=productService.addProduct(addProductDTO);
            ProductDto productDto=productService.convertToDo(product);
            return ResponseEntity.ok(new ApiResponse("success",productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse>getProductById(@PathVariable Long productId)
    {
        try {
            Product product=productService.getProductById(productId);
            ProductDto productDto=productService.convertToDo(product);
            return ResponseEntity.ok(new ApiResponse("success",productDto));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse>updateProduct(@RequestBody ProductUpdateDTO updateDTO,@PathVariable Long productId)
    {
        try {
            Product product=productService.updateProduct(updateDTO,productId);
            ProductDto dto=productService.convertToDo(product);
            return ResponseEntity.ok(new ApiResponse("success",dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse>deleteProduct(@PathVariable Long productId)
    {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("success",productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse>getProductsByBrandAndName(@RequestParam String brandName,@RequestParam String productName)
    {
        try {
            List<Product>products=productService.getProductsByBrandAndName(brandName,productName);
            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with this brand name and product name",null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/category-and-name")
    public ResponseEntity<ApiResponse>getProductsByCategoryAndBrand(@RequestParam String category,@RequestParam String brandName)
    {
        try {
            List<Product>products=productService.getProductsByCategoryAndBrand(category,brandName);
            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with given category name and brand name",null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/name")
    public ResponseEntity<ApiResponse>getProductsByName(@RequestParam String name)
    {
        try {
            List<Product>products=productService.getProductsByName(name);
            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with given name",null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/brand-name")
    public ResponseEntity<ApiResponse>getProductsByBrandName(@RequestParam String brandName)
    {
        try {
            List<Product>products=productService.getProductsByBrand(brandName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with given brand name",null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse>countProductByBrandNameAndName(@RequestParam String brand,@RequestParam String name)
    {
        try {
            var productCount=productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Product Count@",productCount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
}
