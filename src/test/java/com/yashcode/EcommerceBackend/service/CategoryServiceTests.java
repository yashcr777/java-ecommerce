package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.CategoryRepository;
import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.CategoryNotFoundException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category foundCategory = categoryService.getCategoryById(1L);
        assertEquals(category, foundCategory);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void testGetCategoryByName_Success() {
        when(categoryRepository.findByName("Electronics")).thenReturn(category);
        Category foundCategory = categoryService.getCategoryByName("Electronics");
        assertEquals("Electronics", foundCategory.getName());
    }

    @Test
    void testGetCategoryByName_NotFound() {
        when(categoryRepository.findByName("Furniture")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryByName("Furniture"));
    }

    @Test
    void testGetAllCategories_Success() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> result = categoryService.getAllCategories();
        assertEquals(1, result.size());
        assertEquals(category, result.get(0));
    }

    @Test
    void testGetAllCategories_Empty() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.getAllCategories());
    }

    @Test
    void testAddCategory_Success() {
        when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        Category addedCategory = categoryService.addCategory(category);
        assertEquals(category, addedCategory);
    }

    @Test
    void testAddCategory_AlreadyExists() {
        when(categoryRepository.existsByName(category.getName())).thenReturn(true);
        assertThrows(AlreadyExistException.class, () -> categoryService.addCategory(category));
    }

    @Test
    void testUpdateCategory_Success() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.updateCategory(updatedCategory, 1L);
        assertEquals("Updated Electronics", result.getName());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(category, 1L));
    }

    @Test
    void testDeleteCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);
        categoryService.deleteCategoryById(1L);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(1L));
    }

    @Test
    void testSortByField_Success() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(categories);
        List<Category> result = categoryService.sortByField("name");
        assertEquals(1, result.size());
        assertEquals(category, result.get(0));
    }

    @Test
    void testSortByFieldDesc_Success() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "name"))).thenReturn(categories);
        List<Category> result = categoryService.sortByFieldDesc("name");
        assertEquals(1, result.size());
        assertEquals(category, result.get(0));
    }

    @Test
    void testGetCategoryByPagination_Success() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(PageRequest.of(0, 10))).thenReturn(categoryPage);

        Page<Category> result = categoryService.getCategoryByPagination(0, 10);
        assertEquals(categoryPage, result);
    }

    @Test
    void testGetCategoryByPaginationAndSorting_Success() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.DESC, "name")))).thenReturn(categoryPage);

        Page<Category> result = categoryService.getCategoryByPaginationAndSorting(0, 10, "name");
        assertEquals(categoryPage, result);
    }
}