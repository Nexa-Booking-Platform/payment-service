package com.bookingsystem.payment_service.consumer;

import com.bookingsystem.payment_service.entity.Payment;
import com.bookingsystem.payment_service.event.BookingCreatedEvent;
import com.bookingsystem.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "booking-created", groupId = "payment-group")
    public void consume(BookingCreatedEvent event) {

        Payment payment = Payment.builder()
                .bookingId(event.getBookingId())
                .userId(event.getUserId())
                .amount(event.getAmount())
                .providerRef("PAY-" + System.currentTimeMillis())
                .build();

        // simulate success / failure
        boolean success = Math.random() > 0.3; // 70% success

        if (success) {
            payment.setStatus("SUCCESS");

            paymentRepository.save(payment);

            kafkaTemplate.send("payment-success", payment.getBookingId());

        } else {
            payment.setStatus("FAILED");

            paymentRepository.save(payment);

            kafkaTemplate.send("payment-failed", payment.getBookingId());
        }
    }
}