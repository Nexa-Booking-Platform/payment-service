package com.bookingsystem.payment_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String paymentId;
    private String oldStatus;
    private String newStatus;

    private String changedAt;
}