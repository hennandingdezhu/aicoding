package com.aicoding.warehouse.product.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.product.domain.ProductService;
import com.aicoding.warehouse.product.web.ProductRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(String keyword, Long categoryId, Integer status, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("productCode"), like),
                        cb.like(root.get("productName"), like),
                        cb.like(root.get("brand"), like)
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
    }

    @Override
    public Product create(ProductRequest request) {
        productRepository.findByCode(request.productCode())
                .ifPresent(p -> { throw new BusinessException(409, "商品编码已存在"); });
        Product product = new Product();
        apply(request, product);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        productRepository.findByCode(request.productCode())
                .ifPresent(p -> { if (!p.getId().equals(id)) throw new BusinessException(409, "商品编码已存在"); });
        apply(request, product);
        return productRepository.save(product);
    }

    private void apply(ProductRequest request, Product product) {
        product.setProductCode(request.productCode());
        product.setProductName(request.productName());
        product.setCategoryId(request.categoryId());
        product.setSpecification(request.specification());
        product.setUnit(request.unit());
        product.setBrand(request.brand());
        product.setCostPrice(request.costPrice());
        product.setSalePrice(request.salePrice());
        product.setImageFileId(request.imageFileId());
        product.setRemark(request.remark());
    }

    @Override
    public void delete(Long id) {
        Product product = findById(id);
        product.setDeleted(1);
        productRepository.save(product);
    }

    @Override
    public void toggleStatus(Long id, Integer status) {
        Product product = findById(id);
        product.setStatus(status);
        productRepository.save(product);
    }
}
