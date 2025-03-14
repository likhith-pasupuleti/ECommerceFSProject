package com.ecommerce.sb_ecomProject.service;

import com.ecommerce.sb_ecomProject.exception.APIException;
import com.ecommerce.sb_ecomProject.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomProject.model.Category;
import com.ecommerce.sb_ecomProject.model.Product;
import com.ecommerce.sb_ecomProject.payload.ProductDTO;
import com.ecommerce.sb_ecomProject.payload.ProductResponse;
import com.ecommerce.sb_ecomProject.repository.CategoryRepository;
import com.ecommerce.sb_ecomProject.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService
{
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId)
    {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        boolean isProductNotPresent=true;

        List<Product> products=category.getProducts();
        for (Product product : products)
        {
            if (product.getProductName().equals(productDTO.getProductName()))
            {
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent)
        {
            double specialPrice = (productDTO.getPrice()) - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());

            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepository.save(product);

            // Convert Entity to DTO
            ProductDTO responseDTO = modelMapper.map(savedProduct, ProductDTO.class);

            // Explicitly set the category name
            responseDTO.setCategory(savedProduct.getCategory().getCategoryName());

            return responseDTO;
        }
        else
        {
            throw new APIException("Product already exists!!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
    {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                            ?Sort.by(sortBy).ascending()
                            :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepository.findAll(pageDetails);
        List<Product> products=pageProducts.getContent();

        //Product to ProductDto conversion
        List<ProductDTO> productDTOS=products.stream()
                .map(product->modelMapper.map(product, ProductDTO.class))
                .toList();

        if(products.isEmpty())
        {
            throw new APIException("Yet, No Products Added!!!");
        }

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
    {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products=pageProducts.getContent();

        if(products.isEmpty())
        {
            throw new APIException("No Products found with the Category-"+category.getCategoryName());
        }

        //Product to dto conversion
        List<ProductDTO> productDTOS=products.stream()
                .map(product->modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setTotalElements(pageProducts.getTotalElements());

        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
    {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
        List<Product> products=pageProducts.getContent();

        //Product to dto conversion
        List<ProductDTO> productDTOS=products.stream()
                .map(product->modelMapper.map(product, ProductDTO.class))
                .toList();

        if(products.isEmpty())
        {
            throw new APIException("No Products found with the keyword-"+keyword);
        }

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, long productId)
    {
        //Get Existing Product from DB
        Product existingProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productId",productId));

        //Set the existing product details with new details send by user via request body
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());

        double specialPrice=(productDTO.getPrice())-((productDTO.getDiscount()*0.01)*productDTO.getPrice());
        existingProduct.setSpecialPrice(specialPrice);

        //save the updated product
        Product savedUpdatedProduct=productRepository.save(existingProduct);

        //Map the savedUpdatedProduct to ProductDTO
        return modelMapper.map(savedUpdatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(long productId)
    {
        //Get Product from DB
        Product productFromDB=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","product ID",productId));

        productRepository.delete(productFromDB);

        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException
    {
        // Get the product from DB
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "product ID", productId));

        // Upload image
        String fileName = fileService.uploadImage(path, image);

        // Update product image
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);

        // Map to DTO and return
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
