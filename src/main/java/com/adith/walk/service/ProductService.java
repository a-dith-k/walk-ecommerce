package com.adith.walk.service;


import com.adith.walk.dto.ProductDTO;
import com.adith.walk.dto.ProductPageDTO;
import com.adith.walk.entities.Images;
import com.adith.walk.entities.Product;
import com.adith.walk.entities.Size;
import com.adith.walk.entities.Stock;
import com.adith.walk.enums.CustomerCategory;
import com.adith.walk.exceptions.SizeAlreadyExistsException;
import com.adith.walk.repositories.ImageRepo;
import com.adith.walk.repositories.ProductRepo;
import com.adith.walk.repositories.ProductRepository;
import com.adith.walk.repositories.SizeRepository;
import com.adith.walk.service.file.service.FileService;
import com.adith.walk.service.stock.service.StockService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {


    final ProductRepository productRepository;
    final ImageRepo imageRepo;
    final ProductRepo productRepo;
    final CategoryService categoryService;


    final ModelMapper modelMapper;


    final FileService fileService;

    final StockService stockService;

    final SizeRepository sizeRepository;


    public ProductService(ProductRepository productRepository, ImageRepo imageRepo, ProductRepo productRepo, CategoryService categoryService, ModelMapper modelMapper, FileService fileService, StockService stockService, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.imageRepo = imageRepo;
        this.productRepo = productRepo;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.stockService = stockService;
        this.sizeRepository = sizeRepository;
    }

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }


    public Product getProductById(Integer id) {

        Optional<Product> byId = productRepository.findById(id);

        return byId.orElse(new Product());
    }

    public List<Product> getProductByPage(Integer pageNumber, Integer count) {
        Page<Product> all = productRepository.findAll(PageRequest.of(pageNumber, count));
        List<Product> list = new ArrayList<>();
        all.stream().forEach(list::add);
        Pageable pageable = all.nextPageable();
        return list;
    }


    public Product saveProduct(Product product) {
        return productRepository.save(product);

    }


    public static void getStockOfProduct(Product product, String size) {

        Stock stock = product.getStock();


        Size newSize = new Size();
        newSize.setSizeNumber(Short.parseShort(size));
        newSize.setSizeLength((short) 0);
        newSize.setSizeWidth((short) 0);
        newSize.setTotalStock(0);

        List<Size> sizeList = stock.getSizeList();
        sizeList.add(newSize);
        stock.setSizeList(sizeList);

        product.setStock(stock);
    }


    public Product addProduct(ProductDTO addProductRequest, MultipartFile[] images) throws IOException, AlreadyUsedException {
        Product product
                = modelMapper
                .map(addProductRequest, Product.class);

        product
                .setProductDescription(product
                        .getProductDescription()
                        .trim());


        Stock stock = product.getStock();
        stock.setProduct(product);


        if (isProductExists(product)) {
            throw new AlreadyUsedException("Product Name Already Exists");
        }


        for (MultipartFile image : images) {
            if (!Objects.equals(image.getContentType(), "image/jpeg")) {
                throw new IllegalArgumentException("Only JPEG images are Allowed");
            }
        }

        List<Images> imageList = new ArrayList<>();

        for (MultipartFile image : images) {
            Images imageNew = new Images();
            imageNew.setName(fileService.fileUpload(image));
            imageNew.setProduct(product);
            imageList.add(imageNew);
        }
        product.setList(imageList);
        return productRepository.save(product);


    }

    public void updateProduct(ProductDTO productDTO, MultipartFile[] images) throws AlreadyUsedException {

        Product product =
                productRepository
                        .findById(productDTO.getProductId()).orElseThrow();


        if (!product.getProductName().equals(productDTO.getProductName())
                && isProductExists(modelMapper.map(productDTO, Product.class))) {

            throw new AlreadyUsedException("Product with this  name already exists");
        }

        modelMapper.map(productDTO, product);

        Stock stock = product.getStock();
        stock.setTotalStock(0);
        List<Size> sizeList = stock.getSizeList();
        sizeList.forEach(size -> {
            stock.setTotalStock(stock.getTotalStock() + size.getTotalStock());
            size.setStock(stock);
        });


        stock.setProduct(product);
        stockService.save(stock);

        productRepository.save(product);
    }


    public void deleteProduct(Integer productId) {

        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            List<Images> images = product.getList();
            images.forEach(i -> {
                Path path = Paths.get("src/main/resources/static/img/productImages/" + i.getName());
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException("images were not deleted");
                }
            });
            productRepository.delete(product);
        }


    }

    public Product removeProduct(Integer productId) {

        return productRepository.removeProductByProductId(productId);
    }

    public List<Product> getProductByNameContaining(String query) {

        return productRepository.findByProductNameContaining(query);
    }

    public Product getProductByName(String name) {

        return productRepository.findByProductName(name);
    }


    public ProductPageDTO getPageOfProducts(Integer pageNumber, Integer noOfProducts) {


        System.out.println("here----------------------");

        Pageable pageable = PageRequest.of(pageNumber, noOfProducts);

        Page<Product> all = productRepository.findAll(pageable);

        ProductPageDTO responseDTO = new ProductPageDTO();
        responseDTO.setProducts(all);
        responseDTO.setCurrentPageNumber(all.getNumber());
        responseDTO.setTotalPages(all.getTotalPages());

        return responseDTO;
    }

    public ProductPageDTO getMenProducts(Integer pageNumber, Integer count) {

        Page<Product> menProducts = productRepository.findProductsByCustomerCategory(CustomerCategory.MALE, PageRequest.of(pageNumber, count));

        ProductPageDTO productPageDTO = new ProductPageDTO();
        productPageDTO.setTotalPages(menProducts.getTotalPages());
        productPageDTO.setCurrentPageNumber(menProducts.getNumber());
        productPageDTO.setProducts(menProducts);

        return productPageDTO;
    }


    public ProductPageDTO getWomenProducts(Integer pageNumber, Integer count) {

        Page<Product> womenProducts = productRepository.findProductsByCustomerCategory(CustomerCategory.FEMALE, PageRequest.of(pageNumber, count));

        ProductPageDTO productPageDTO = new ProductPageDTO();
        productPageDTO.setTotalPages(womenProducts.getTotalPages());
        productPageDTO.setCurrentPageNumber(womenProducts.getNumber());
        productPageDTO.setProducts(womenProducts);

        return productPageDTO;
    }


    private boolean isProductExists(Product product) {
        return productRepository.findByProductName(product.getProductName()) != null;
    }


    public void addSize(long stockId, short sizeNumber) throws SizeAlreadyExistsException {


        Stock stock = stockService.getStockById(stockId);
        for (Size size : stock.getSizeList()) {
            if (size.getSizeNumber() == sizeNumber) {
                throw new SizeAlreadyExistsException("Size Already Available");
            }
        }

        Size newSize = new Size();
        newSize.setSizeWidth((short) 0);
        newSize.setSizeLength((short) 0);
        newSize.setSizeNumber(sizeNumber);
        newSize.setTotalStock(0);
        newSize.setStock(stock);

        stock.getSizeList().add(newSize);

        stockService.save(stock);

    }


    public void updateStock(Integer productId, Stock stock) {
        stock.setProduct(getProductById(productId));
        stock.getSizeList().forEach(size -> {
            stock.setTotalStock(stock.getTotalStock() + size.getTotalStock());
            size.setStock(stock);
        });
        stockService.save(stock);
    }


    public Long getProductTax(long totalAmount, int taxPercent) {

        return totalAmount * taxPercent / 100;
    }
}
