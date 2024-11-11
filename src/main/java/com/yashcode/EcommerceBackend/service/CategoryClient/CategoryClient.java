package com.yashcode.EcommerceBackend.service.CategoryClient;


import com.yashcode.EcommerceBackend.entity.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="http://localhost:8082",value="Category-Client")
public interface CategoryClient {
    @GetMapping("/api/v1/categories/category/{name}/categoryByName")
    Category getCategoryByName(@PathVariable String name);
}
