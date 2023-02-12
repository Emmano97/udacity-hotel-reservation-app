package com.sossahills.hotel.service;

import com.sossahills.hotel.model.Customer;
import com.sossahills.hotel.model.IRoom;
import com.sossahills.hotel.model.Reservation;

import java.util.*;

public final class ReservationService {

    private static ReservationService reservationService;
    private  Set<Reservation> reservations;
    private  Map<String, IRoom> rooms;

    public ReservationService() {
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public static ReservationService getOrCreateInstance() {
        if (reservationService == null)
            reservationService = new ReservationService();
        return reservationService;
    }


    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber()))
            throw new IllegalArgumentException("The room number " + room.getRoomNumber() +
                    " already exists!");
        else
            rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        if (!rooms.containsKey(roomId))
            throw new IllegalArgumentException("The room number " +
                    roomId + "doesn't exists!");
        else {
            return rooms.get(roomId);
        }
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate,
                                    Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        if (reservations.contains(newReservation)) {
            throw new IllegalArgumentException("This room is already reserved for these " +
                    "days");
        }
        reservations.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Map<String, IRoom> availableRooms = new HashMap<>(this.rooms);
        for (Reservation aReservation: this.reservations) {
            DatesCheckResult validatedDates = validateDates(aReservation, checkInDate,
                    checkOutDate);
            boolean isCheckInOK = validatedDates.isCheckInOK();
            boolean isCheckOutOK = validatedDates.isCheckOutOK();
            if (! isCheckInOK || ! isCheckOutOK) {
                availableRooms.remove(aReservation.getRoom().getRoomNumber());
            }
        }
        return new ArrayList<>(availableRooms.values());
    }

    DatesCheckResult validateDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean isCheckInOK = checkInDate.before(reservation.getCheckInDate()) ||
                checkInDate.compareTo(reservation.getCheckOutDate()) >= 0;
        boolean isCheckOutOK = checkOutDate.compareTo(reservation.getCheckInDate()) <= 0 ||
                checkOutDate.after(reservation.getCheckOutDate());
        return new DatesCheckResult(isCheckInOK, isCheckOutOK);
    }

    public Collection<Reservation> getReservations(Customer customer) {
        List<Reservation> customersReservations = new ArrayList<>();
        for (Reservation aReservation: this.reservations) {
            if (aReservation.getCustomer().equals(customer)) {
                customersReservations.add(aReservation);
            }
        }
        return customersReservations;
    }

    public Map<String, IRoom> getRooms() {
        return rooms;
    }

    record DatesCheckResult(boolean isCheckInOK, boolean isCheckOutOK) {}
}
