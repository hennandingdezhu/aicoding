package com.aicoding.warehouse.customer.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.customer.domain.CustomerService;
import com.aicoding.warehouse.customer.web.CustomerRequest;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(String keyword, Integer status, Pageable pageable) {
        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("customerCode"), like),
                        cb.like(root.get("customerName"), like),
                        cb.like(root.get("contactPerson"), like)
                ));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return customerRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "客户不存在"));
    }

    @Override
    public Customer create(CustomerRequest request) {
        customerRepository.findByCode(request.customerCode())
                .ifPresent(c -> { throw new BusinessException(409, "客户编码已存在"); });
        Customer customer = new Customer();
        apply(request, customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, CustomerRequest request) {
        Customer customer = findById(id);
        customerRepository.findByCode(request.customerCode())
                .ifPresent(c -> { if (!c.getId().equals(id)) throw new BusinessException(409, "客户编码已存在"); });
        apply(request, customer);
        return customerRepository.save(customer);
    }

    private void apply(CustomerRequest request, Customer customer) {
        customer.setCustomerCode(request.customerCode());
        customer.setCustomerName(request.customerName());
        customer.setContactPerson(request.contactPerson());
        customer.setContactPhone(request.contactPhone());
        customer.setAddress(request.address());
        customer.setRemark(request.remark());
    }

    @Override
    public void delete(Long id) {
        Customer customer = findById(id);
        customer.setDeleted(1);
        customerRepository.save(customer);
    }
}
