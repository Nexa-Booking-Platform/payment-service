package com.bookingsystem.payment_service.repository;

import com.bookingsystem.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}