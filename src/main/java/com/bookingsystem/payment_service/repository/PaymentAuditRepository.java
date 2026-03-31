package com.bookingsystem.payment_service.repository;

import com.bookingsystem.payment_service.entity.PaymentAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentAuditRepository extends JpaRepository<PaymentAudit, String> {
}