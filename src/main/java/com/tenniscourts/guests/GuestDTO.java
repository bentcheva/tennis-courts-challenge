package com.tenniscourts.guests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "GuestDTO", description = "POJO DTO model for Guest entity")
public class GuestDTO {

    @ApiModelProperty(value = "id", required = true, example = "0")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "name", required = true, example = "Boris Becker")
    @NotNull
    private String name;
}
