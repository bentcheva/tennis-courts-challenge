package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    //test  >=24 hours before start date_time for 100% refund
    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(1000));
    }

    //test  >=12 && <24 hours before start date_time for 75% refund
    @Test
    public void getRefundValue75PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusHours(15);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(750));
    }



    //test  >=2 && <12 hours before start date_time for 50% refund
    @Test
    public void getRefundValue50PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusHours(5);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(500));
    }

    //test  >=00:01 && <2 hours before start date_time for 25% refund
    @Test
    public void getRefundValue25PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusHours(1);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(250));
    }

    //test exactly 24 hours before start date_time for 100% refund
    @Test
    public void getRefundValueFullRefundEdgeCase() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusHours(24);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(1000));
    }

    //test  edge case exactly 12 hours before start date_time for 75% refund
    @Test
    public void getRefundValue75PercentRefundEdgeCase() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusHours(12);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(750));
    }

    //test  exactly 2 hours before start date_time for 50% refund
    @Test
    public void getRefundValue50PercentRefundEdgeCase() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusHours(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(500));
    }

    //test  exactly 1 min before start date_time for 25% refund
    @Test
    public void getRefundValue25PercentRefundEdgeCase() {
        Schedule schedule = new Schedule();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.plusMinutes(1);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(1000L)).build(), localDateTime), new BigDecimal(250));
    }
}
