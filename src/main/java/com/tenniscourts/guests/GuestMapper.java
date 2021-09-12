package com.tenniscourts.guests;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    Guest map(GuestDTO guest);

    GuestDTO map(Guest guest);

    List<GuestDTO> map(List<Guest> guests);
}
