package com.example.bai3.controller;

import com.example.bai3.exception.InvalidProductRequestException;
import com.example.bai3.exception.ProductExistsException;
import com.example.bai3.exception.ProductNotFoundException;
import com.example.bai3.model.Product;
import com.example.bai3.model.ResponseObject;
import com.example.bai3.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllProducts() {
        List<Product> list = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all product successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProductById(@PathVariable Long id) {
        Product foundProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query product successfully", foundProduct));
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> insertProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidProductRequestException("Invalid product data");
        }

        if (productRepository.existsByName(product.getName())) {
            throw new ProductExistsException("Product name = " + product.getName() + " already exists.");
        }

        Product newProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(HttpStatus.OK, "Insert product successfully", newProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProduct(@Valid @RequestBody Product newproduct, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            throw new InvalidProductRequestException("Invalid product data");
        }

        Product foundProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (productRepository.existsByName(newproduct.getName()) && !foundProduct.getName().toLowerCase().equals(newproduct.getName().trim().toLowerCase())) {
            throw new ProductExistsException("Product name = " + newproduct.getName() + " already exists.");
        }

        newproduct.setId(id);
        Product updateProduct = productRepository.save(newproduct);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update product with id = " + id + " successfully", updateProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete product with id = " + id + " successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchProducts(@RequestParam(required = false) String keyword) {
        if (keyword == null) {
            List<Product> list = productRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all product successfully", list));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query products successfully", productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)));
    }

    @GetMapping("/price")
    public ResponseEntity<ResponseObject> searchCheapProducts(@RequestParam(defaultValue = "0.0") double maxPrice) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query products successfully", productRepository.findCheapProducts(maxPrice)));
    }

    @GetMapping("/page")
    public ResponseEntity<ResponseObject> getProducts(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable;
        if (size > 0) {
            pageable = PageRequest.of(page, size);
        } else {
            pageable = Pageable.unpaged();
        }

        Page<Product> products = productRepository.findAll(pageable);
        ResponseObject responseObject = new ResponseObject(HttpStatus.OK, "Query all products successfully", products.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

}
