package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    // Find all guests
    public List<GuestDTO> findAll() {
        return guestMapper.map(guestRepository.findAll());
    }

    //Find guest by id
    public GuestDTO findById(Long guestId) {
        return guestMapper.map(isGuestPresent(guestRepository, guestId));
    }

    // Find guest by name. The method is case-insensitive. In order to tolerate name collisions the method returns List.
    public List<GuestDTO> findByName(String name) {
        List<Guest> guests = guestRepository.findByNameIgnoreCase(name);
        if(guests.isEmpty()) {
            // throw exception if a guest with such name does not exist
            throw new EntityNotFoundException(MessageFormat.format("Guest with name {0} not found.", name));
        }
        return guestMapper.map(guests);
    }

    // Persist the guest
    public GuestDTO save(Guest guest) {
        return guestMapper.map(guestRepository.save(guest));
    }

    // Add new guest
    public GuestDTO addGuest(GuestDTO createGuestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(createGuestDTO)));
    }

    // Delete existing guest by Id, if such guest does not exist throw an exception
    public void deleteGuest(Long guestId) {
        guestRepository.delete(isGuestPresent(guestRepository, guestId));
    }

    // Update existent guest, if such guest does not exist throw an exception
    public GuestDTO updateGuest(GuestDTO guestToBeUpdated) {
        isGuestPresent(guestRepository, guestToBeUpdated.getId());
        // now can save the updated guest
        return guestMapper.map(guestRepository.save(guestMapper.map(guestToBeUpdated)));
    }

    // Helper method to validate if guest with guestId exists
    private Guest isGuestPresent(GuestRepository guestRepository, Long guestId) throws EntityNotFoundException {
        Optional<Guest> result = guestRepository.findById(guestId);
        Guest guest = null;
        // sanity check that the guest exists
        if (result.isPresent()) {
            guest = result.get();
        } else {
            // throw exception if a guest with id does not exist
            throw new EntityNotFoundException(MessageFormat.format("Guest with id {0} not found.", guestId));
        }
        return guest;
    }
}
