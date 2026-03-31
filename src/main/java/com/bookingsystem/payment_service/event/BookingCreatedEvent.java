package com.bookingsystem.payment_service.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {

    private String bookingId;
    private String userId;
    private double amount;
}