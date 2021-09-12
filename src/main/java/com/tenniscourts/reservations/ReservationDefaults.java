package com.tenniscourts.reservations;

public enum ReservationDefaults {
    // The dollars are converted to cents as we work with BigDecimal
    RESERVATION_VALUE(1000),
    RESERVATION_REFUND_75(75),
    RESERVATION_REFUND_50(50),
    RESERVATION_REFUND_25(25);

    private final int value;

    ReservationDefaults(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
