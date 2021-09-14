package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleRequestDTO {

    @NotNull
    private Long tennisCourtId;

    @ApiModelProperty(notes = "start date", example = "2021-09-20T20:00:00.0")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.S")
    @NotNull
    private LocalDateTime startDateTime;

}
