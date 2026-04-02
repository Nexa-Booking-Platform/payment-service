package com.bookingsystem.payment_service.service.impl;

import com.bookingsystem.payment_service.service.PaymentGateway;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MockPaymentGateway implements PaymentGateway {

    private final Random random = new Random();

    @Override
    public boolean processPayment(String bookingId, String userId, double amount) {
        // Simulate a call to an external payment provider (Stripe, PayPal, etc.)
        // 80% success rate for simulation
        return random.nextDouble() < 0.8;
    }
}