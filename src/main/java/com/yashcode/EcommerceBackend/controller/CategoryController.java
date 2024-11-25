package com.yashcode.EcommerceBackend.controller;


import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories()
    {
        try {
            List<Category> categoryList=categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found",categoryList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",null));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>createCategory(@RequestBody Category name){
        try {
            Category category=categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Success",category));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse>getCategoryById(@PathVariable Long id){
        try {
            Category category=categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse>getCategoryByName(@PathVariable String name){
        try {
            Category category=categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found by name",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse>deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse>updateCategory(@PathVariable Long id,@RequestBody Category category)
    {
        try {
            Category updatedCategory=categoryService.updateCategory(category,id);
            return ResponseEntity.ok(new ApiResponse("Updated Successfully",updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/sort/{field}")
    public List<Category>sortCategory(@PathVariable String field){
        return categoryService.sortByField(field);
    }
    @GetMapping("/sortdesc/{field}")
    public List<Category>sortCategoryByDesc(@PathVariable String field){
        return categoryService.sortByFieldDesc(field);
    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public List<Category> categoryPagination(@PathVariable int offset, @PathVariable int pageSize){
        return categoryService.getCategoryByPagination(offset,pageSize).getContent();
    }
    @GetMapping("/paginationAndSorting/{offset}/{pageSize}/{field}")
    public List<Category> categoriesPaginationAndSorting(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field){
        return categoryService.getCategoryByPaginationAndSorting(offset,pageSize,field).getContent();
    }
}
