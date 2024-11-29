package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.category.ICategoryService;
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
 class CategoryControllerTests {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryController=new CategoryController(categoryService);
    }

    @Test
    void testGetAllCategories_Success() {
        
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        when(categoryService.getAllCategories()).thenReturn(categories);

        
        ResponseEntity<ApiResponse> response = categoryController.getAllCategories();

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetAllCategories_Failure() {
        
        when(categoryService.getAllCategories()).thenThrow(new ResourceNotFoundException("No categories found"));

        
        ResponseEntity<ApiResponse> response = categoryController.getAllCategories();

        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testCreateCategory_Success() {
        
        Category category = new Category();
        when(categoryService.addCategory(any(Category.class))).thenReturn(category);

        
        ResponseEntity<ApiResponse> response = categoryController.createCategory(category);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testCreateCategory_AlreadyExist() {
        
        when(categoryService.addCategory(any(Category.class))).thenThrow(new AlreadyExistException("Category already exists"));

        
        ResponseEntity<ApiResponse> response = categoryController.createCategory(new Category());

        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category already exists", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryById_Success() {
        
        Category category = new Category();
        when(categoryService.getCategoryById(anyLong())).thenReturn(category);

        
        ResponseEntity<ApiResponse> response = categoryController.getCategoryById(1L);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryById_NotFound() {
        
        when(categoryService.getCategoryById(anyLong())).thenThrow(new ResourceNotFoundException("Category not found"));

        
        ResponseEntity<ApiResponse> response = categoryController.getCategoryById(1L);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryByName_Success() {
        
        Category category = new Category();
        when(categoryService.getCategoryByName(anyString())).thenReturn(category);

        
        ResponseEntity<ApiResponse> response = categoryController.getCategoryByName("Electronics");

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found by name", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryByName_NotFound() {
        
        when(categoryService.getCategoryByName(anyString())).thenThrow(new ResourceNotFoundException("Category not found"));

        
        ResponseEntity<ApiResponse> response = categoryController.getCategoryByName("Electronics");

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDeleteCategory_Success() {
        
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(1L);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDeleteCategory_NotFound() {
        
        doThrow(new ResourceNotFoundException("Category not found")).when(categoryService).deleteCategoryById(anyLong());

        
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(1L);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdateCategory_Success() {
        
        Category category = new Category();
        when(categoryService.updateCategory(any(Category.class), anyLong())).thenReturn(category);

        
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1L, category);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testUpdateCategory_NotFound() {
        
        when(categoryService.updateCategory(any(Category.class), anyLong())).thenThrow(new ResourceNotFoundException("Category not found"));

        
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1L, new Category());

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testSortCategory_Success() {
        
        List<Category> sortedCategories = new ArrayList<>();
        sortedCategories.add(new Category());
        when(categoryService.sortByField(anyString())).thenReturn(sortedCategories);

        
        List<Category> result = categoryController.sortCategory("name");

        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSortCategoryByDesc_Success() {
        
        List<Category> sortedCategories = new ArrayList<>();
        sortedCategories.add(new Category());
        when(categoryService.sortByFieldDesc(anyString())).thenReturn(sortedCategories);

        
        List<Category> result = categoryController.sortCategoryByDesc("name");

        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCategoryPagination_Success() {
        
        List<Category> paginatedCategories = new ArrayList<>();
        when(categoryService.getCategoryByPagination(anyInt(), anyInt()).getContent()).thenReturn(paginatedCategories);

        
        List<Category> result = categoryController.categoryPagination(0, 10);

        
        assertNotNull(result);
    }

    @Test
    void testCategoriesPaginationAndSorting_Success() {
        
        List<Category> paginatedCategories = new ArrayList<>();
        when(categoryService.getCategoryByPaginationAndSorting(anyInt(), anyInt(), anyString()).getContent()).thenReturn(paginatedCategories);

        
        List<Category> result = categoryController.categoriesPaginationAndSorting(0, 10, "name");

        
        assertNotNull(result);
    }
}
