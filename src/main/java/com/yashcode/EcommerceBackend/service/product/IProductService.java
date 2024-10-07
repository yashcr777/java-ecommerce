package com.yashcode.EcommerceBackend.service.product;


import com.yashcode.EcommerceBackend.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.dto.ProductDto;
import com.yashcode.EcommerceBackend.dto.ProductUpdateDTO;
import com.yashcode.EcommerceBackend.entity.Product;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductDTO addProductDTO);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateDTO product, Long productId);
    List<Product>getAllProducts();
    List<Product>getProductByCategory(String category);
    List<Product>getProductsByBrand(String brand);
    List<Product>getProductsByCategoryAndBrand(String category,String brand);
    List<Product>getProductsByName(String name);
    List<Product>getProductsByBrandAndName(String brand,String name);
    Long countProductsByBrandAndName(String brand,String name);
    ProductDto convertToDo(Product product);
    List<ProductDto>getConvertedProducts(List<Product>products);
}
