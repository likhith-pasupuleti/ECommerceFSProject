package com.ecommerce.sb_ecomProject.controller;

import com.ecommerce.sb_ecomProject.config.AppConstants;
import com.ecommerce.sb_ecomProject.payload.ProductDTO;
import com.ecommerce.sb_ecomProject.payload.ProductResponse;
import com.ecommerce.sb_ecomProject.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController
{
    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId)
    {
        return new ResponseEntity<>(productService.addProduct(productDTO,categoryId), HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                          @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                          @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIRECTION) String sortOrder)
    {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId,
                                                                @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                                @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIRECTION) String sortOrder)
    {
        return new ResponseEntity<>(productService.getProductByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
                                                               @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                               @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                               @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                               @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIRECTION) String sortOrder)
    {
        return new ResponseEntity<>(productService.getProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,@PathVariable long productId)
    {
        return new ResponseEntity<>(productService.updateProduct(productDTO,productId),HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long productId)
    {
        return new ResponseEntity<>(productService.deleteProduct(productId),HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable long productId,
                                             @RequestParam("image")MultipartFile image) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId,image),HttpStatus.OK);
    }
}
