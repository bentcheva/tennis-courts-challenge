package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    public final TennisCourtRepository tennisCourtRepository;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        Optional<TennisCourt> result = tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId());
        TennisCourt tennisCourt = null;
        if( result.isPresent()) {
            tennisCourt = result.get();
        } else {
            // throw exception that tennis court with id does not exist
            throw new EntityNotFoundException("Tennis court not found.");
        }
        Schedule schedule = new Schedule();
        schedule.setTennisCourt(tennisCourt);
        schedule.setStartDateTime(createScheduleRequestDTO.getStartDateTime());
        // assuming that a standard tennis match continues for 3 hours, so schedule the court for 3 hours
        schedule.setEndDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(3));
        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findByStartDateTimeAndEndDateTime(startDate, endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
