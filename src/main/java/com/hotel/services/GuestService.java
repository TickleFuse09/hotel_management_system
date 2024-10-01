package com.hotel.services;

import com.hotel.models.Guest;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class GuestService {
    private List<Guest> guests = new ArrayList<>();
    private int lastGuestId = 0;
    private List<String> allRooms = Arrays.asList("101", "102", "103", "104", "105");

    public void addGuest(Guest guest) {
        lastGuestId++;
        guest.setId(lastGuestId);
        guests.add(guest);
    }

    public List<Guest> getAllGuests() {
        return guests;
    }

    public int getLastGuestId() {
        return lastGuestId;
    }

    public void checkOutGuest(int guestId) {
        guests.removeIf(guest -> guest.getId() == guestId);
    }

    public List<String> getEmptyRooms() {
        List<String> occupiedRooms = new ArrayList<>();
        for (Guest guest : guests) {
            occupiedRooms.add(guest.getRoomNumber());
        }
        List<String> emptyRooms = new ArrayList<>(allRooms);
        emptyRooms.removeAll(occupiedRooms);
        return emptyRooms;
    }
}
