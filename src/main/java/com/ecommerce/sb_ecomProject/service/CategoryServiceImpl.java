package com.ecommerce.sb_ecomProject.service;

import com.ecommerce.sb_ecomProject.exception.APIException;
import com.ecommerce.sb_ecomProject.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomProject.model.Category;
import com.ecommerce.sb_ecomProject.payload.CategoryDTO;
import com.ecommerce.sb_ecomProject.payload.CategoryResponse;
import com.ecommerce.sb_ecomProject.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService
{
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
    {
        //Sortby
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        //Pagination
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage=categoryRepository.findAll(pageDetails);
        List<Category> categories=categoryPage.getContent();

        if(categories.isEmpty())
            throw new APIException("No category created till now!!");

        List<CategoryDTO> categoryDTOS=categories.stream()
                .map(category->modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO)
    {
        Category category=modelMapper.map(categoryDTO,Category.class);

        Category findCategory=categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

        if(findCategory!=null)
            throw new APIException("Category with this name "+category.getCategoryName()+" already exists!!!");

        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId)
    {
        Category deletedCategory=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(deletedCategory);
        return modelMapper.map(deletedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId)
    {
        // Find the existing category
        Category updatedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        // Check if another category already has the same name
        Category existingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (existingCategory != null && !existingCategory.getCategoryId().equals(categoryId))
        {
            throw new APIException("Category with this name '" + categoryDTO.getCategoryName() + "' already exists!");
        }

        // Update only if the category name is changing
        if (!updatedCategory.getCategoryName().equals(categoryDTO.getCategoryName()))
        {
            updatedCategory.setCategoryName(categoryDTO.getCategoryName());
        }

        // Save the updated category
        updatedCategory = categoryRepository.save(updatedCategory);

        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

}
