package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;

@Api(value = "Guest")
@AllArgsConstructor
@RequestMapping("/guests")
@RestController
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @ApiOperation(value = "Add new guest")
    @PostMapping
    public ResponseEntity<Void> addGuest(@ApiParam(name = "createGuestRequest", value = "GuestDTO model to add guest", required = true) @RequestBody GuestDTO createGuestRequest) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(createGuestRequest).getId())).build();
    }

    @ApiOperation(value = "Get all guests")
    @GetMapping
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @ApiOperation(value = "Get guest by guest Id")
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@ApiParam(name = "guestId", example = "2", value = "Guest id", required = true) @PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findById(guestId));
    }

    @ApiOperation(value = "Get guest by name")
    @GetMapping("/byname/{name}")
    public ResponseEntity<List<GuestDTO>> findGuestByName(@ApiParam(name = "name", example = "Rafael Nadal", value = "Guest name", required = true) @PathVariable String name) {
        return ResponseEntity.ok(guestService.findByName(name));
    }

    @ApiOperation(value = "Delete guest by guest Id")
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(@ApiParam(name = "guestId", example = "2", value = "Guest id", required = true) @PathVariable long guestId) {
        guestService.deleteGuest(guestId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update guest")
    @PutMapping
    public ResponseEntity<GuestDTO> updateGuest(@ApiParam(name = "guestToBeUpdated", value = "GuestDTO model to update guest", required = true)@RequestBody GuestDTO guestToBeUpdated) {
        return ResponseEntity.ok(guestService.updateGuest(guestToBeUpdated));
    }
}
