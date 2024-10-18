package com.yashcode.EcommerceBackend.service.category;

import com.yashcode.EcommerceBackend.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category>getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category,Long id);
    void deleteCategoryById(Long id);
    List<Category>sortByField(String field);
    Page<Category> getCategoryByPagination(int offset, int pageSize);
    List<Category>sortByFieldDesc(String field);
    Page<Category> getCategoryByPaginationAndSorting(int offset, int pageSize,String field);
}
