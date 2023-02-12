package com.sossahills.hotel;

import com.sossahills.hotel.api.AdminResource;
import com.sossahills.hotel.api.HotelResource;
import com.sossahills.hotel.model.Customer;
import com.sossahills.hotel.service.CustomerService;
import com.sossahills.hotel.service.ReservationService;
import com.sossahills.hotel.ui.MainMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HotelApplication {
    public static void test(String[] args){
        Customer customer = new Customer("First", "Second", "j@domain.com");
        System.out.println(customer);

        // Test Email validation
        Customer invalidEmailCustomer = new Customer("First", "Second", "email");
    }

    public static void main(String[] args){
        ReservationService reservationService = ReservationService.getOrCreateInstance();
        CustomerService customerService = CustomerService.getOrCreateInstance();
        AdminResource adminResource = new AdminResource(customerService, reservationService);
        HotelResource hotelResource = new HotelResource(customerService, reservationService);
        Scanner scanner = new Scanner(System.in);
        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        MainMenu mainMenu = new MainMenu(now, hotelResource, adminResource, scanner, simpleDateFormat);
        mainMenu.run();
    }

}
