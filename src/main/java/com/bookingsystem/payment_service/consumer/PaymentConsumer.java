package com.bookingsystem.payment_service.consumer;

import com.bookingsystem.payment_service.entity.Payment;
import com.bookingsystem.payment_service.event.BookingCreatedEvent;
import com.bookingsystem.payment_service.repository.PaymentRepository;
import com.bookingsystem.payment_service.producer.PaymentProducer;
import com.bookingsystem.payment_service.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;
    private final PaymentGateway paymentGateway;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "${kafka.booking.topics.booking-created:booking-created-topic}", groupId = "payment-group")
    public void consume(BookingCreatedEvent event) {
        log.info("Received booking created event: {}", event);

        Payment payment = Payment.builder()
                .bookingId(event.getBookingId())
                .userId(event.getUserId())
                .amount(event.getAmount())
                .providerRef("PAY-" + System.currentTimeMillis())
                .build();

        // Use the payment gateway service for a more realistic simulation
        boolean success = paymentGateway.processPayment(event.getBookingId(), event.getUserId(), event.getAmount());

        if (success) {
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);
            paymentProducer.sendPaymentSuccess(payment.getBookingId());
            log.info("Payment processed successfully for booking: {}", event.getBookingId());

        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            paymentProducer.sendPaymentFailed(payment.getBookingId());
            log.error("Payment processing failed for booking: {}", event.getBookingId());
        }
    }
}