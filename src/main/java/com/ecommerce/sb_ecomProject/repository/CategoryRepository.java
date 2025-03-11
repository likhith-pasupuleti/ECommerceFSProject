package com.ecommerce.sb_ecomProject.repository;

import com.ecommerce.sb_ecomProject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>
{
    Category findByCategoryName(String categoryName);
}
