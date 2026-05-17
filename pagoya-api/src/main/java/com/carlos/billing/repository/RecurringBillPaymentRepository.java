package com.carlos.billing.repository;

import com.carlos.billing.model.RecurringBillPayment;
import com.carlos.billing.model.RecurringStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface RecurringBillPaymentRepository extends JpaRepository<RecurringBillPayment, Long> {

    @Query("SELECT r FROM RecurringBillPayment r WHERE r.customer.id = :customerId")
    List<RecurringBillPayment> findByCustomerId(@Param("customerId") Long customerId);

    List<RecurringBillPayment> findByStatusAndNextRunAtLessThanEqual(RecurringStatus status, LocalDateTime dateTime);
}