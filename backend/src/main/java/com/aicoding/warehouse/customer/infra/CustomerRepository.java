package com.aicoding.warehouse.customer.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    @Query("SELECT c FROM Customer c WHERE c.customerCode = :code AND c.deleted = 0")
    Optional<Customer> findByCode(@Param("code") String code);
    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.deleted = 0")
    Optional<Customer> findByIdActive(@Param("id") Long id);
}
