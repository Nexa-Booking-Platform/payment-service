package com.bookingsystem.payment_service.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.payment.topics.payment-success:payment-success-topic}")
    private String paymentSuccessTopic;

    @Value("${kafka.payment.topics.payment-failed:payment-failed-topic}")
    private String paymentFailedTopic;

    public PaymentProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentSuccess(String bookingId) {
        kafkaTemplate.send(paymentSuccessTopic, bookingId);
    }

    public void sendPaymentFailed(String bookingId) {
        kafkaTemplate.send(paymentFailedTopic, bookingId);
    }
}