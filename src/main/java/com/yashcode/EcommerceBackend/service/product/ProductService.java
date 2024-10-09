package com.yashcode.EcommerceBackend.service.product;


import com.yashcode.EcommerceBackend.Repository.CategoryRepository;
import com.yashcode.EcommerceBackend.Repository.ImageRepository;
import com.yashcode.EcommerceBackend.Repository.ProductRepository;
import com.yashcode.EcommerceBackend.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.dto.ImageDto;
import com.yashcode.EcommerceBackend.dto.ProductDto;
import com.yashcode.EcommerceBackend.dto.ProductUpdateDTO;
import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Image;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductDTO addProductDTO) {
        if(productExists(addProductDTO.getName(),addProductDTO.getBrand()))
        {
            throw new AlreadyExistException(addProductDTO.getBrand()+" "+addProductDTO.getName()+" already exists");
        }
        Category category= Optional.ofNullable(categoryRepository.findByName(addProductDTO.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory=new Category(addProductDTO.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        addProductDTO.setCategory(category);
        return productRepository.save(createProduct(addProductDTO,category));
    }

    private boolean productExists(String name,String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductDTO dto, Category category)
    {
        return new Product(
                dto.getName(),
                dto.getBrand(),
                dto.getPrice(),
                dto.getInventory(),
                dto.getDescription(),
                Arrays.asList(category)
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->{throw new ProductNotFoundException("Product not found");});
    }

    private Product updateExistingProducts(Product existingProduct, ProductUpdateDTO productUpdateDTO)
    {
        existingProduct.setName(productUpdateDTO.getName());
        existingProduct.setPrice(productUpdateDTO.getPrice());
        existingProduct.setBrand(productUpdateDTO.getBrand());
        existingProduct.setDescription(productUpdateDTO.getDescription());
        existingProduct.setInventory(productUpdateDTO.getInventory());
        Category category=categoryRepository.findByName(productUpdateDTO.getCategory().getName());
        if(category==null){
            category=new Category(productUpdateDTO.getCategory().getName());
            List<Category>categories=existingProduct.getCategory();
            categories.add(category);
            existingProduct.setCategory(categories);
        }
        return existingProduct;
    }
    @Override
    public Product updateProduct(ProductUpdateDTO productUpdateDTO, Long productId) {
        System.out.println(productUpdateDTO);
        return productRepository.findById(productId)
                .map(existingProduct->updateExistingProducts(existingProduct,productUpdateDTO))
                .map(productRepository::save)
                .orElseThrow(()->new ProductNotFoundException("Product not Found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
    @Override
    public ProductDto convertToDo(Product product){

        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        Category category = new Category();
        category.setId(product.getCategory().get(0).getId());
        category.setName(product.getCategory().get(0).getName());
        productDto.setCategory(product.getCategory());
        productDto.setBrand(product.getBrand());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        return productDto;
    }
    @Override
    public List<ProductDto>getConvertedProducts(List<Product>products) {
        return products.stream().map(this::convertToDo).toList();
    }
}
