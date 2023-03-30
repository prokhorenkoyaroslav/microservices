package com.dmd.productservice;

import com.dmd.productservice.dto.ProductRequest;
import com.dmd.productservice.dto.ProductResponse;
import com.dmd.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
class EcommerceApplicationTests {


    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ProductService productService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

 /*   @Test
    void shouldGetAllProducts() {
        // Given
        productService.createProduct(new ProductRequest("Product 1", "Product 1", new BigDecimal(10)));
        productService.createProduct(new ProductRequest("Product 2", "Product 2", new BigDecimal(12)));
        // When
        List <ProductResponse> products = productService.getAllProducts();
        // Then
        System.out.println(products.size());
        Assertions.assertEquals(products.size(), 3);
        Assertions.assertEquals(products.get(0).getName(), "Product 1");
        Assertions.assertEquals(products.get(0).getDescription(), "Product 1");
        Assertions.assertEquals(products.get(0).getPrice(), new BigDecimal(10));

        Assertions.assertEquals(products.get(1).getName(), "Product 2");
        Assertions.assertEquals(products.get(1).getDescription(), "Product 2");
        Assertions.assertEquals(products.get(1).getPrice(), new BigDecimal(12));

    }*/

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productService.getAllProducts().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Iphone 14")
                .description("Iphone 14")
                .price(BigDecimal.valueOf(1000))
                .build();
    }

}
