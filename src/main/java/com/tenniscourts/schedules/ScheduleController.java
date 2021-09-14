package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Api("Schedules")
@AllArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "Add schedule to the tennis court")
    @PostMapping
    public ResponseEntity<Void> addScheduleTennisCourt(@ApiParam(name = "createScheduleRequestDTO",
            value = "CreateScheduleRequestDTO add schedule to tennis court", required = true)
                                                       @RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Find schedules by Id")
    @GetMapping("/bydates")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@ApiParam(name = "startDate", example = "2021-09-20T20:00:00.0", value = "Schedule start date", required = true)
                                                                  @RequestParam("startDate")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
                                                                  @ApiParam(name = "endDate", example = "2021-09-20T21:00:00.0", value = "Schedule end date", required = true)
                                                                  @RequestParam("endDate")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59, 0))));
    }

    @ApiOperation(value = "Find schedule by schedule Id")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@ApiParam(name = "scheduleId", example = "1", value = "Schedule id", required = true)
                                                        @PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
