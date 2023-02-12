package com.sossahills.hotel.api;

import com.sossahills.hotel.model.Customer;
import com.sossahills.hotel.model.IRoom;
import com.sossahills.hotel.model.Reservation;
import com.sossahills.hotel.service.CustomerService;
import com.sossahills.hotel.service.ReservationService;

import java.util.Collection;
import java.util.Date;


public final class HotelResource {

    private static HotelResource hotelResource;
    private static CustomerService customerService;
    private static ReservationService reservationService;

    public static HotelResource getOrCreateInstance(){
        if (hotelResource == null)
            hotelResource = new HotelResource(customerService, reservationService);
        return hotelResource;
    }

    public HotelResource(CustomerService p_customerService, ReservationService p_reservationService) {
        customerService = p_customerService;
        reservationService = p_reservationService;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createCustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate,
                                 Date checkOutDate) {
        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate,
                checkOutDate);
    }

    public Collection<Reservation> getReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        return reservationService.getReservations(customer);
    }

    public Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate) {
        return reservationService.findRooms(checkInDate, checkOutDate);
    }
}
