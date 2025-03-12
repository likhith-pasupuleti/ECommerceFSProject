package com.ecommerce.sb_ecomProject.service;

import com.ecommerce.sb_ecomProject.model.Category;
import com.ecommerce.sb_ecomProject.payload.CategoryDTO;
import com.ecommerce.sb_ecomProject.payload.CategoryResponse;

import java.util.List;

public interface CategoryService
{
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);
}
