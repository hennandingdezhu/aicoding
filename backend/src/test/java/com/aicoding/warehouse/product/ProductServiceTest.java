package com.aicoding.warehouse.product;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.product.domain.ProductService;
import com.aicoding.warehouse.product.infra.Product;
import com.aicoding.warehouse.product.infra.ProductRepository;
import com.aicoding.warehouse.productcategory.infra.ProductCategory;
import com.aicoding.warehouse.productcategory.infra.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private Long categoryId;

    @BeforeEach
    void setUp() {
        ProductCategory cat = new ProductCategory();
        cat.setCategoryCode("CAT-001");
        cat.setCategoryName("电子产品");
        cat.setSortOrder(0);
        cat.setStatus(1);
        productCategoryRepository.save(cat);
        categoryId = cat.getId();

        Product p = new Product();
        p.setProductCode("SKU001");
        p.setProductName("机械键盘");
        p.setCategoryId(categoryId);
        p.setSpecification("87键");
        p.setUnit("个");
        p.setBrand("Demo");
        p.setCostPrice(new BigDecimal("100.0000"));
        p.setSalePrice(new BigDecimal("199.0000"));
        p.setRemark("测试商品");
        p.setStatus(1);
        productRepository.save(p);
    }

    @Test
    void shouldCreateProduct() {
        var request = new com.aicoding.warehouse.product.web.ProductRequest(
                "SKU002", "鼠标", categoryId, "无线", "个", "Demo", new BigDecimal("50.0000"), new BigDecimal("99.0000"), null, "测试鼠标");

        Product created = productService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getProductCode()).isEqualTo("SKU002");
        assertThat(created.getProductName()).isEqualTo("鼠标");
        assertThat(created.getStatus()).isEqualTo(1);
    }

    @Test
    void shouldRejectDuplicateProductCode() {
        var request = new com.aicoding.warehouse.product.web.ProductRequest(
                "SKU001", "键盘2", categoryId, "108键", "个", "Demo", new BigDecimal("100.0000"), new BigDecimal("199.0000"), null, "test");

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("商品编码已存在");
    }

    @Test
    void shouldFindById() {
        Long id = productRepository.findAll().get(0).getId();
        Product found = productService.findById(id);
        assertThat(found.getProductCode()).isEqualTo("SKU001");
        assertThat(found.getProductName()).isEqualTo("机械键盘");
    }

    @Test
    void shouldFindAllWithPagination() {
        Page<Product> page = productService.findAll("机械", categoryId, null, PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateProduct() {
        Long id = productRepository.findAll().get(0).getId();
        var request = new com.aicoding.warehouse.product.web.ProductRequest(
                "SKU001", "机械键盘Pro", categoryId, "108键", "个", "Demo", new BigDecimal("120.0000"), new BigDecimal("249.0000"), null, "升级版");

        Product updated = productService.update(id, request);
        assertThat(updated.getProductName()).isEqualTo("机械键盘Pro");
        assertThat(updated.getCostPrice()).isEqualByComparingTo("120.0000");
    }

    @Test
    void shouldSoftDeleteProduct() {
        Long id = productRepository.findAll().get(0).getId();
        productService.delete(id);
        Product deleted = productRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }

    @Test
    void shouldToggleProductStatus() {
        Long id = productRepository.findAll().get(0).getId();
        productService.toggleStatus(id, 0);
        Product disabled = productRepository.findById(id).orElseThrow();
        assertThat(disabled.getStatus()).isEqualTo(0);
    }
}
