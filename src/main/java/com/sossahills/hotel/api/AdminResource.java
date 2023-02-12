package com.sossahills.hotel.api;

import com.sossahills.hotel.model.Customer;
import com.sossahills.hotel.model.IRoom;
import com.sossahills.hotel.model.Reservation;
import com.sossahills.hotel.service.CustomerService;
import com.sossahills.hotel.service.ReservationService;

import java.util.*;


public final class AdminResource {

    private static AdminResource adminResource;
    private static CustomerService customerService;
    private static ReservationService reservationService;

    public AdminResource(CustomerService p_customerService, ReservationService p_reservationService){
        customerService = p_customerService;
        reservationService = p_reservationService;
    }

    public static AdminResource getOrCreateInstance(){
        if (adminResource == null)
            adminResource = new AdminResource(customerService, reservationService);
        return adminResource;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom newRoom: rooms)
            reservationService.addRoom(newRoom);
    }

    public Collection<IRoom> getAllRooms() {
        Map<String, IRoom> allRooms = reservationService.getRooms();
        if (allRooms != null)
            return new ArrayList<>(allRooms.values());
        return null;
    }

    public Collection<Customer> getAllCustomers() {
        Collection<Customer> customers = customerService.getAllCustomers();;
        if (customers != null) return customers;
        else return null;
    }

    public Set<Reservation> getAllReservations() {
        return reservationService.getReservations();
    }

    public void displayAllReservations(){
        getAllCustomers().forEach(reservation -> System.out.println(reservation.toString()));
    }
}
