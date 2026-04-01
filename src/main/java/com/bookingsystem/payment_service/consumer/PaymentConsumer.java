package com.bookingsystem.payment_service.consumer;

import com.bookingsystem.payment_service.entity.Payment;
import com.bookingsystem.payment_service.event.BookingCreatedEvent;
import com.bookingsystem.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

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
                .status("SUCCESS")
                .providerRef("PAY-" + System.currentTimeMillis())
                .build();

        paymentRepository.save(payment);

        // send next event
        kafkaTemplate.send("payment-success", payment.getBookingId());
    }
}