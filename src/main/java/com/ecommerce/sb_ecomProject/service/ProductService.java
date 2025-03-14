package com.ecommerce.sb_ecomProject.service;

import com.ecommerce.sb_ecomProject.model.Product;
import com.ecommerce.sb_ecomProject.payload.ProductDTO;
import com.ecommerce.sb_ecomProject.payload.ProductResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService
{
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse getProductByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse getProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, long productId);

    ProductDTO deleteProduct(long productId);

    ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException;
}
