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
class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCategories_Success() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act
        ResponseEntity<ApiResponse> response = categoryController.getAllCategories();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetAllCategories_Failure() {
        // Arrange
        when(categoryService.getAllCategories()).thenThrow(new ResourceNotFoundException("No categories found"));

        // Act
        ResponseEntity<ApiResponse> response = categoryController.getAllCategories();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testCreateCategory_Success() {
        // Arrange
        Category category = new Category();
        when(categoryService.addCategory(any(Category.class))).thenReturn(category);

        // Act
        ResponseEntity<ApiResponse> response = categoryController.createCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testCreateCategory_AlreadyExist() {
        // Arrange
        when(categoryService.addCategory(any(Category.class))).thenThrow(new AlreadyExistException("Category already exists"));

        // Act
        ResponseEntity<ApiResponse> response = categoryController.createCategory(new Category());

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category already exists", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryById_Success() {
        // Arrange
        Category category = new Category();
        when(categoryService.getCategoryById(anyLong())).thenReturn(category);

        // Act
        ResponseEntity<ApiResponse> response = categoryController.getCategoryById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Arrange
        when(categoryService.getCategoryById(anyLong())).thenThrow(new ResourceNotFoundException("Category not found"));

        // Act
        ResponseEntity<ApiResponse> response = categoryController.getCategoryById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryByName_Success() {
        // Arrange
        Category category = new Category();
        when(categoryService.getCategoryByName(anyString())).thenReturn(category);

        // Act
        ResponseEntity<ApiResponse> response = categoryController.getCategoryByName("Electronics");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Found by name", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetCategoryByName_NotFound() {
        // Arrange
        when(categoryService.getCategoryByName(anyString())).thenThrow(new ResourceNotFoundException("Category not found"));

        // Act
        ResponseEntity<ApiResponse> response = categoryController.getCategoryByName("Electronics");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDeleteCategory_Success() {
        // Act
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDeleteCategory_NotFound() {
        // Arrange
        doThrow(new ResourceNotFoundException("Category not found")).when(categoryService).deleteCategoryById(anyLong());

        // Act
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdateCategory_Success() {
        // Arrange
        Category category = new Category();
        when(categoryService.updateCategory(any(Category.class), anyLong())).thenReturn(category);

        // Act
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1L, category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testUpdateCategory_NotFound() {
        // Arrange
        when(categoryService.updateCategory(any(Category.class), anyLong())).thenThrow(new ResourceNotFoundException("Category not found"));

        // Act
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1L, new Category());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testSortCategory_Success() {
        // Arrange
        List<Category> sortedCategories = new ArrayList<>();
        sortedCategories.add(new Category());
        when(categoryService.sortByField(anyString())).thenReturn(sortedCategories);

        // Act
        List<Category> result = categoryController.sortCategory("name");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSortCategoryByDesc_Success() {
        // Arrange
        List<Category> sortedCategories = new ArrayList<>();
        sortedCategories.add(new Category());
        when(categoryService.sortByFieldDesc(anyString())).thenReturn(sortedCategories);

        // Act
        List<Category> result = categoryController.sortCategoryByDesc("name");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCategoryPagination_Success() {
        // Arrange
        List<Category> paginatedCategories = new ArrayList<>();
        when(categoryService.getCategoryByPagination(anyInt(), anyInt()).getContent()).thenReturn(paginatedCategories);

        // Act
        List<Category> result = categoryController.categoryPagination(0, 10);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testCategoriesPaginationAndSorting_Success() {
        // Arrange
        List<Category> paginatedCategories = new ArrayList<>();
        when(categoryService.getCategoryByPaginationAndSorting(anyInt(), anyInt(), anyString()).getContent()).thenReturn(paginatedCategories);

        // Act
        List<Category> result = categoryController.categoriesPaginationAndSorting(0, 10, "name");

        // Assert
        assertNotNull(result);
    }
}
