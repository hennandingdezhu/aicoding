package com.aicoding.warehouse.productcategory;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.productcategory.domain.ProductCategoryService;
import com.aicoding.warehouse.productcategory.infra.ProductCategory;
import com.aicoding.warehouse.productcategory.infra.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductCategoryServiceTest {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private Long parentId;

    @BeforeEach
    void setUp() {
        ProductCategory parent = new ProductCategory();
        parent.setParentId(null);
        parent.setCategoryCode("CAT-001");
        parent.setCategoryName("电子产品");
        parent.setSortOrder(0);
        parent.setStatus(1);
        productCategoryRepository.save(parent);
        parentId = parent.getId();

        ProductCategory child = new ProductCategory();
        child.setParentId(parentId);
        child.setCategoryCode("CAT-002");
        child.setCategoryName("手机");
        child.setSortOrder(0);
        child.setStatus(1);
        productCategoryRepository.save(child);
    }

    @Test
    void shouldCreateCategory() {
        var request = new com.aicoding.warehouse.productcategory.web.CategoryRequest(
                parentId, "CAT-003", "电脑", 1);

        ProductCategory created = productCategoryService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getCategoryCode()).isEqualTo("CAT-003");
        assertThat(created.getCategoryName()).isEqualTo("电脑");
        assertThat(created.getParentId()).isEqualTo(parentId);
        assertThat(created.getStatus()).isEqualTo(1);
    }

    @Test
    void shouldRejectDuplicateCategoryCode() {
        var request = new com.aicoding.warehouse.productcategory.web.CategoryRequest(
                null, "CAT-001", "重复", 1);

        assertThatThrownBy(() -> productCategoryService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("分类编码已存在");
    }

    @Test
    void shouldReturnTree() {
        List<com.aicoding.warehouse.productcategory.web.CategoryTreeResponse> tree = productCategoryService.getTree();

        assertThat(tree).hasSize(1);
        assertThat(tree.get(0).categoryName()).isEqualTo("电子产品");
        assertThat(tree.get(0).children()).hasSize(1);
        assertThat(tree.get(0).children().get(0).categoryName()).isEqualTo("手机");
    }

    @Test
    void shouldUpdateCategory() {
        Long id = productCategoryRepository.findByCode("CAT-001").orElseThrow().getId();
        var request = new com.aicoding.warehouse.productcategory.web.CategoryRequest(
                null, "CAT-001", "电子产品-已更新", 2);

        ProductCategory updated = productCategoryService.update(id, request);
        assertThat(updated.getCategoryName()).isEqualTo("电子产品-已更新");
        assertThat(updated.getSortOrder()).isEqualTo(2);
    }

    @Test
    void shouldSoftDeleteCategory() {
        Long id = productCategoryRepository.findByCode("CAT-001").orElseThrow().getId();
        productCategoryService.delete(id);

        ProductCategory deleted = productCategoryRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }

    @Test
    void shouldFindById() {
        Long id = productCategoryRepository.findByCode("CAT-001").orElseThrow().getId();
        ProductCategory found = productCategoryService.findById(id);
        assertThat(found.getCategoryName()).isEqualTo("电子产品");
    }
}
