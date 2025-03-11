package com.ecommerce.sb_ecomProject.service;

import com.ecommerce.sb_ecomProject.exception.APIException;
import com.ecommerce.sb_ecomProject.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomProject.model.Category;
import com.ecommerce.sb_ecomProject.payload.CategoryResponse;
import com.ecommerce.sb_ecomProject.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService
{
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories=categoryRepository.findAll();

        if(categories.isEmpty())
            throw new APIException("No category created till now!!");

        return categories;
    }

    @Override
    public String createCategory(Category category)
    {
        Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());

        if(savedCategory!=null)
            throw new APIException("Category with this name "+category.getCategoryName()+" already exists!!!");

        categoryRepository.save(category);
        return "Category Added Successfully!";
    }

    @Override
    public String deleteCategory(Long categoryId)
    {
        Optional<Category> deleteCategory=categoryRepository.findById(categoryId);

        Category deletedCategory=deleteCategory
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(deletedCategory);
        return "Category with id:"+categoryId+" Deleted Successfully!";
    }

    @Override
    public String updateCategory(Category category,Long categoryId)
    {
        Optional<Category> updateCategory=categoryRepository.findById(categoryId);

        Category updatedCategory=updateCategory
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        updatedCategory.setCategoryName(category.getCategoryName());
        categoryRepository.save(updatedCategory);

        return "Category with id:"+categoryId+" is updated!";
    }
}
