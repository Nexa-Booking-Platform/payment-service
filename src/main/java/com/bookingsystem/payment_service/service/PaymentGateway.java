package com.bookingsystem.payment_service.service;

public interface PaymentGateway {
    boolean processPayment(String bookingId, String userId, double amount);
}