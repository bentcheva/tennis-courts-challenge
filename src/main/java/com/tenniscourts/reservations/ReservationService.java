package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        Reservation reservation = reservationMapper.map(createReservationRequestDTO);
        reservation.setValue(new BigDecimal(ReservationDefaults.RESERVATION_VALUE.getValue()));
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        // upon booking the reservation qualifies for full refund, so setting the refund value to the reservation value.
        reservation.setRefundValue(reservation.getValue());
        // persist the reservation and then map in order to return the DTO
        return reservationMapper.map(reservationRepository.save(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId, ReservationStatus.CANCELLED));
    }

    private Reservation cancel(Long reservationId, ReservationStatus reservationStatus) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation, LocalDateTime.now());
            return this.updateReservation(reservation, refundValue, reservationStatus);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    /*
        Refactored the method signature to make the method "stateless" in terms of the time snapshot provided by LocalDateTime.now().
        This makes the unit testing of the method time independent.
     */
    public BigDecimal getRefundValue(Reservation reservation, LocalDateTime localDateTime) {
        long minutes = ChronoUnit.MINUTES.between(localDateTime, reservation.getSchedule().getStartDateTime());
        BigDecimal refundValue = BigDecimal.ZERO;

        // full refund when cancellation/rescheduling happens more than 24 hours from the start
        if (minutes >= TimeUnit.HOURS.toMinutes(24)) {
            refundValue = reservation.getValue();
        }
        // penalty band 25%
        // 75% refund when cancellation/rescheduling happens >= 12hours and <= 23 h 59 mins from the start
        if (minutes >= TimeUnit.HOURS.toMinutes(12) && minutes < TimeUnit.HOURS.toMinutes(24)) {
            refundValue = getReservationRefundByPenaltyBands(reservation, ReservationDefaults.RESERVATION_REFUND_75.getValue());
        }
        // penalty band 50%
        // 50% refund when cancellation/rescheduling happens >= 2hours and <= 11 h 59 mins from the start
        if (minutes >= TimeUnit.HOURS.toMinutes(2) && minutes < TimeUnit.HOURS.toMinutes(12)) {
            refundValue = getReservationRefundByPenaltyBands(reservation, ReservationDefaults.RESERVATION_REFUND_50.getValue());
        }

        // penalty band 75%
        // 25% refund when cancellation/rescheduling happens >= 1 min and <= 2 hours from the start
        if (minutes >= 1 && minutes < TimeUnit.HOURS.toMinutes(2)) {
            refundValue = getReservationRefundByPenaltyBands(reservation, ReservationDefaults.RESERVATION_REFUND_25.getValue());
        }
        return refundValue;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        // do not perform status update yet, just update RefundValue and Value based on the time of reschedule
        Reservation previousReservation = cancel(previousReservationId, ReservationStatus.READY_TO_PLAY);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    private BigDecimal getReservationRefundByPenaltyBands(Reservation reservation, int penaltyPercent) {
        long refundValue = reservation.getValue().longValue() * penaltyPercent / 100;
        return new BigDecimal(refundValue);
    }
}
