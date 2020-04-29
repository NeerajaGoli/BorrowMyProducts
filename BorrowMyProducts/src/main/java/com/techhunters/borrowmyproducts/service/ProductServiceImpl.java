package com.techhunters.borrowmyproducts.service;

import java.util.ArrayList;
import java.util.List;

import com.techhunters.borrowmyproducts.entity.ProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techhunters.borrowmyproducts.daorepository.ProductRepository;
import com.techhunters.borrowmyproducts.dto.ProductDTO;
import com.techhunters.borrowmyproducts.entity.Product;
import com.techhunters.borrowmyproducts.objectmappers.ProductMapper;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRequestService productRequestService;

    @Override
    public List<ProductDTO> listAll() {
        List<ProductDTO> productDTOs = new ArrayList<>();
        List<Product> products = (List<Product>) productRepository.findAll();
        for (Product product : products) {
            ProductDTO productDTO = productMapper.convertToDTO(product);
            productDTOs.add(productDTO);
        }
        return productDTOs;
    }

    @Override
    public ProductDTO findById(int id) {
        Product product = productRepository.findById(id).get();
        return productMapper.convertToDTO(product);
    }

    @Override
    public ProductDTO findByName(String name) {
        return productMapper.convertToDTO(productRepository.findByProductName(name));
    }


    @Override
    public void save(ProductDTO productDTO) {
        productRepository.save(productMapper.convertToEntity(productDTO));

    }

    @Override
    public void delete(ProductDTO productDTO) {
        productRepository.delete(productMapper.convertToEntity(productDTO));

    }

    @Override
    public List<ProductDTO> listByCategoryName(String name) {
        List<ProductDTO> allProducts = listAll();
        List<ProductDTO> products = new ArrayList<>();
        for (ProductDTO productDTO : allProducts) {
            if (productDTO.getCategory().getCategoryName().equals(name)) {
                products.add(productDTO);
            }
        }
        return products;
    }

    @Override
    public List<ProductDTO> listByUserId(int id) {
        List<ProductDTO> allProducts = listAll();
        List<ProductDTO> products = new ArrayList<>();
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getUser().getUserId() == (id)) {
                products.add(allProducts.get(i));
            }
        }
        return products;
    }

    @Override
    public List<ProductDTO> listAvailableProductsByCategory(String catName, int id) {
        List<ProductDTO> products = listByCategoryName(catName);
        List<Integer> requests = productRequestService.listByUserId(id);
        List<ProductDTO> available = new ArrayList<>();
        for (ProductDTO productDTO : products) {
            if (productDTO.getProductStatus().equals("available") && productDTO.getUser().getUserId() != id && !requests.contains(productDTO.getProductId())) {
                available.add(productDTO);
            }
        }
        return available;
    }
}
 