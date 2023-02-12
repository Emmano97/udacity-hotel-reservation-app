package com.sossahills.hotel.model;

import java.util.Date;

public class Reservation {

    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate,
                Date checkOutDate) {
        this.room = room;
        this.customer = customer;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public IRoom getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public final int hashCode() {
        int result = 7;
        if (room != null)
            result = 31 * result + room.hashCode();
        if (checkInDate != null)
            result = 31 * result + checkInDate.hashCode();
        if (checkOutDate != null)
            result = 31 * result + checkOutDate.hashCode();
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Reservation other))
            return false;
        boolean roomsEquals = (room == null && other.getRoom() == null) ||
                (room != null && room.equals(other.getRoom()));
        boolean checkInEquals = (checkInDate == null && other.getCheckInDate() == null)
                || (checkInDate != null && checkInDate.equals(other.getCheckInDate()));
        boolean checkOutEquals = (checkOutDate == null && other.getCheckOutDate() == null)
                || (checkOutDate != null && checkOutDate.equals(other.getCheckOutDate()));
        return roomsEquals && checkInEquals && checkOutEquals;
    }

    @Override
    public String toString() {
        return "Reservation:" + System.lineSeparator() +
                "Customer: " + customer + System.lineSeparator() +
                "Room: " + room + System.lineSeparator() +
                "Check In <-: " + checkInDate + " - Check Out ->" + checkOutDate;
    }
}
