package com.carlos.billing.service;

import com.carlos.billing.model.BillPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {

    @Query("SELECT b FROM BillPayment b WHERE b.customer.id = :customerId")
    Page<BillPayment> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    boolean existsByCustomer_IdAndProvider_IdAndBillCode(Long customerId, Long providerId, String billCode);

    @Query(value = "SELECT * FROM fn_get_payments_by_category(:customerId)", nativeQuery = true)
    java.util.List<Object[]> getPaymentsByCategory(@Param("customerId") Long customerId);
}