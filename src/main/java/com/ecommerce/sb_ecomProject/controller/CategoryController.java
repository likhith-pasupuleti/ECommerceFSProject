package com.ecommerce.sb_ecomProject.controller;

import com.ecommerce.sb_ecomProject.model.Category;
import com.ecommerce.sb_ecomProject.payload.CategoryResponse;
import com.ecommerce.sb_ecomProject.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<List<Category>> getAllCategories()
    {
        return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category)
    {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category Added Successfully!!!",HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId)
    {
        String status= categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(status);

    }

    @PutMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId)
    {
        String status= categoryService.updateCategory(category,categoryId);
        return new ResponseEntity<>("Category with category id: "+categoryId, HttpStatus.OK);
    }
}
