package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api("Reservations")
@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation(value = "Book reservation")
    @PostMapping
    public ResponseEntity<Void> bookReservation(@ApiParam(name = "createReservationRequestDTO", value = "CreateReservationRequestDTO model to book reservation", required = true)
                                                @RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Find reservation by reservation id")
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@ApiParam(name = "reservationId", value = "Reservation id", required = true)
                                                          @PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    public ResponseEntity<ReservationDTO> rescheduleReservation(Long reservationId, Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
