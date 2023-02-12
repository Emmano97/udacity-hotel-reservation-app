package com.sossahills.hotel.model;

import java.util.regex.Pattern;

public class Customer {

    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String firstName, String lastName, String email) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid Email format");
        } else {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile("^(.+)@(.+)[.](.+)$")
                .matcher(email).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + ", " + email + ".";
    }
}
