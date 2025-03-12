package com.ecommerce.sb_ecomProject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO
{
    private Long categoryId;
    private String categoryName;
}
