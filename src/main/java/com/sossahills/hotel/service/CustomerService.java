package com.sossahills.hotel.service;

import com.sossahills.hotel.model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CustomerService {

    private final Map<String, Customer> customers;

    private static CustomerService customerService;

    private CustomerService() {
        this.customers = new HashMap<>();
    }


    public static CustomerService getOrCreateInstance() {
        if (customerService == null)
            customerService = new CustomerService();
        return customerService;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        if (customers.containsKey(email)) {
            throw new IllegalArgumentException("A customer with the same email already exists.");
        } else {
            customers.put(email, newCustomer);
        }
    }

    public Customer getCustomer(String customerEmail) {
        return this.customers.getOrDefault(customerEmail, null);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
