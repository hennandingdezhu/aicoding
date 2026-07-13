package com.aicoding.warehouse.productcategory.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.productcategory.domain.ProductCategoryService;
import com.aicoding.warehouse.productcategory.web.CategoryRequest;
import com.aicoding.warehouse.productcategory.web.CategoryTreeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getTree() {
        List<ProductCategory> all = productCategoryRepository.findAllActive();
        Map<Long, List<ProductCategory>> childrenMap = all.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(ProductCategory::getParentId));

        return all.stream()
                .filter(c -> c.getParentId() == null)
                .map(c -> buildTree(c, childrenMap))
                .collect(Collectors.toList());
    }

    private CategoryTreeResponse buildTree(ProductCategory category, Map<Long, List<ProductCategory>> childrenMap) {
        List<CategoryTreeResponse> children = childrenMap.getOrDefault(category.getId(), List.of())
                .stream()
                .map(c -> buildTree(c, childrenMap))
                .collect(Collectors.toList());
        return new CategoryTreeResponse(
                category.getId(), category.getParentId(), category.getCategoryCode(),
                category.getCategoryName(), category.getSortOrder(), category.getStatus(), children);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategory findById(Long id) {
        return productCategoryRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "分类不存在"));
    }

    @Override
    public ProductCategory create(CategoryRequest request) {
        productCategoryRepository.findByCode(request.categoryCode())
                .ifPresent(c -> { throw new BusinessException(409, "分类编码已存在"); });
        ProductCategory category = new ProductCategory();
        category.setParentId(request.parentId());
        category.setCategoryCode(request.categoryCode());
        category.setCategoryName(request.categoryName());
        category.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
        return productCategoryRepository.save(category);
    }

    @Override
    public ProductCategory update(Long id, CategoryRequest request) {
        ProductCategory category = findById(id);
        productCategoryRepository.findByCode(request.categoryCode())
                .ifPresent(c -> { if (!c.getId().equals(id)) throw new BusinessException(409, "分类编码已存在"); });
        category.setParentId(request.parentId());
        category.setCategoryCode(request.categoryCode());
        category.setCategoryName(request.categoryName());
        category.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
        return productCategoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        ProductCategory category = findById(id);
        category.setDeleted(1);
        productCategoryRepository.save(category);
    }
}
