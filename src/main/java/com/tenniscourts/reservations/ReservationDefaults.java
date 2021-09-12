package com.tenniscourts.reservations;

public enum ReservationDefaults {
    RESERVATION_VALUE(50);

    private final int value;

    ReservationDefaults(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}

