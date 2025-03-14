package com.ecommerce.sb_ecomProject.controller;

import com.ecommerce.sb_ecomProject.config.AppConstants;
import com.ecommerce.sb_ecomProject.model.Category;
import com.ecommerce.sb_ecomProject.payload.CategoryDTO;
import com.ecommerce.sb_ecomProject.payload.CategoryResponse;
import com.ecommerce.sb_ecomProject.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController
{
    CategoryService categoryService;

    @Autowired
    private CategoryController(CategoryService categoryService)
    {
        this.categoryService=categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                             @RequestParam(value="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                             @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false) String sortBy,
                                                             @RequestParam(value="sortOrder",defaultValue = AppConstants.SORT_DIRECTION,required = false) String sortOrder)
    {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO)
    {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId)
    {
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId),HttpStatus.OK);
    }

    @PutMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId)
    {
        return new ResponseEntity<>(categoryService.updateCategory(categoryDTO,categoryId), HttpStatus.OK);
    }
}
