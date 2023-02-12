package com.sossahills.hotel.ui;

import com.sossahills.hotel.api.AdminResource;
import com.sossahills.hotel.api.HotelResource;
import com.sossahills.hotel.model.IRoom;
import com.sossahills.hotel.model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {

    private final Date now;
    private final HotelResource hotelResource;
    private final AdminResource adminResource;
    private final DateFormat simpleDateFormat;
    private final Scanner scanner;

    
    public MainMenu(Date now, HotelResource hotelResource, AdminResource adminResource, Scanner scanner, DateFormat simpleDateFormat) {
        this.now = now;
        this.hotelResource = hotelResource;
        this.adminResource = adminResource;
        this.scanner = scanner;
        this.simpleDateFormat = simpleDateFormat;
    }

    public void printMenu() {
        System.out.println("");
        System.out.println("Welcome to SOSSA Hills Hotel Reservation App");
        System.out.println("----------------------------------------");
        System.out.println("Please enter \n");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("----------------------------------------");
        
    }

    public void displayReservations() {
        System.out.println("Please enter your email");
        String email = getEmail();
        if (!isCustomerExists(email))
            System.out.println("Please create an account");
        Collection<Reservation> customerReservations = hotelResource.getReservations(email);

        if (customerReservations == null && customerReservations.isEmpty()) {
            System.out.println("We can't find a reservation");
        } else {
            System.out.println("Reservations for :"+email);
            for (Reservation reservation: customerReservations) {
                System.out.println(reservation);
            }
        }
    }

    private String getEmail() {
        boolean isReading = true;
        String email = "";
        while (isReading) {
            String inputData = scanner.nextLine();
            if (!isValidEmail(inputData)){
                System.out.println("Invalid Email");
                continue;
            }
            email = inputData;
            isReading = false;
        }
        return email;
    }

    private boolean isValidEmail(String input) {
        return Pattern.compile("^(.+)@(.+).(.+)$").matcher(input).matches();
    }

    private boolean isCustomerExists(String email) {
        return hotelResource.getCustomer(email) != null;
    }

    public void createNewAccount() {
        boolean isAdding = true;
        while (isAdding) {
            System.out.println("Enter your email");
            String email = getEmail();
            if (isCustomerExists(email)) {
                System.out.println("Customer with the same email already Exists");
                continue;
            }
            System.out.println("Enter your first name");
            String firstName = getName(true);
            System.out.println("Enter your last name");
            String lastName = getName(false);
            isAdding = false;
            hotelResource.createCustomer(email, firstName, lastName);
            System.out.println("Customer successfully created");
        }
    }

    private String getName(boolean isFirstName) {
        String name = "";
        String inputData;
        String nameType = "last";
        if (isFirstName) {
            nameType = "first";
        }
        boolean isReading = true;
        while (isReading) {
            inputData = scanner.nextLine();
            if (!hasCharacters(inputData)) {
                System.out.println("Your " + nameType + " name should have at " +
                        "least one letter.");
                continue;
            }
            name = inputData;
            isReading = false;
        }
        return name;
    }

    private boolean hasCharacters(String input) {
        return input.matches(".*[a-zA-Z]+.*");
    }

    public void findAndReserveARoom() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Enter check-in date in format mm/dd/yyyy");
            Date checkInDate = getDate();
            System.out.println("Enter check-out date in format mm/dd/yyyy ");
            Date checkOutDate = getDate();
            if (checkInDate.after(checkOutDate)) {
                System.out.println("The check-in date can't be later than checkout " +
                        "date. try again");
                continue;
            }
            Collection<IRoom> availableRooms = findAvailableRooms(checkInDate, checkOutDate);
            if (availableRooms.isEmpty()) {
                isRunning = false;
                continue;
            }
            System.out.println("Available rooms for booking:");
            for (IRoom room: availableRooms) {
                System.out.println(room);
            }
            if (stopBooking()) {
                isRunning = false;
                continue;
            }
            if (isCustomerHasAccount()) {
                isRunning = false;
                continue;
            }
            System.out.println("Please enter your email");
            String email = getEmail();
            if (! isCustomerExists(email)) {
                System.out.println("Please create an account");
                isRunning = false;
                continue;
            }
            String roomNumber = getReservationRoomNumer(availableRooms);
            IRoom room = hotelResource.getRoom(roomNumber);
            Reservation newReservation = hotelResource.bookARoom(email, room,
                    checkInDate, checkOutDate);
            System.out.println(newReservation);
            isRunning = false;
        }
    }

    private Date getDate() {
        boolean isReading = true;
        Date date = null;
        while (isReading) {
            String input = scanner.nextLine();
            if (isValidDate(input)) {
                try {
                    simpleDateFormat.setLenient(false);
                    date = simpleDateFormat.parse(input);
                } catch (ParseException ex) {
                    System.out.println("Try again");
                    continue;
                }
                if (! date.before(now)) isReading = false;
                else System.out.println("This date is in the past. Please reenter the date");
            } else System.out.println("Renter the date in format mm/dd/yyyy");
        }
        return date;
    }

    private boolean isValidDate(String input) {
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(input);
        } catch (ParseException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

        return true;
    }

    private Collection<IRoom> findAvailableRooms(Date checkIn, Date checkOut) {
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn,
                checkOut);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms found for selected dates. Trying to find" +
                    " a room in the next 7 days");

            // Shift dates
            checkIn = shiftDate(checkIn);
            checkOut = shiftDate(checkOut);

            // Find rooms available for shifted dates
            availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No free rooms in the next 7 days found. Try " +
                        "different dates");
            } else {
                // Print shifted dates and available rooms
                System.out.println("You can book following rooms from " + checkIn +
                        " till " + checkOut + ":");
                for (IRoom aRoom: availableRooms) {
                    System.out.println(aRoom);
                }
            }
        }
        return availableRooms;
    }

    private Date shiftDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    private boolean stopBooking() {
        System.out.println("Would you like to book one of the rooms above? " +
                "(y/n)");
        boolean stopBooking = false;
        boolean keepReadingAnswer = true;
        while (keepReadingAnswer) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y" -> // Proceed with booking
                        keepReadingAnswer = false;
                case "n" -> {
                    keepReadingAnswer = false;
                    stopBooking = true;
                }
                default -> // Keep asking
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return stopBooking;
    }

    private boolean isCustomerHasAccount() {
        System.out.println("Do you have an account? (y/n)");
        boolean customerHasNoAccount = false;
        boolean keepReadingAnswer = true;
        while (keepReadingAnswer) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y" -> // Proceed with booking
                        keepReadingAnswer = false;
                case "n" -> {
                    // Go to main menu
                    System.out.println("Please create an account in main menu");
                    customerHasNoAccount = true;
                    keepReadingAnswer = false;
                }
                default -> // Keep asking
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return customerHasNoAccount;
    }

    boolean isNumber(String strInt) {
        if (strInt == null) {
            return false;
        }
        try {
            Double.parseDouble(strInt);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private String getReservationRoomNumer(Collection<IRoom> availableRooms) {
        System.out.println("Please enter room number");
        String roomNumber = "";
        boolean isReading = true;
        while (isReading) {
            String input = scanner.nextLine();
            if (isNumber(input)) {
                boolean isAvailableRoom = false;
                for (IRoom aRoom: availableRooms) {
                    if (aRoom.getRoomNumber().equals(input)) {
                        isAvailableRoom = true;
                        break;
                    }
                }
                if (isAvailableRoom) {
                    isReading = false;
                    roomNumber = input;
                } else System.out.println("The room you picked is not available. Please from the the list above ");
            } else System.out.println("Room number should be an integer number");
        }
        return roomNumber;
    }

    public void run(){
        AdminMenu adminMenu = new AdminMenu(adminResource, scanner);
        boolean isRunning = true;
        while (isRunning) {
            try {
                this.printMenu();
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1 -> this.findAndReserveARoom();
                    case 2 -> this.displayReservations();
                    case 3 -> this.createNewAccount();
                    case 4 -> adminMenu.run();
                    case 5 -> {
                        System.out.println("Exiting...");
                        isRunning = false;
                        scanner.close();
                    }
                    default -> System.out.println("Please enter a number from above list");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a number");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            } catch (Exception ex) {
                System.out.println("Unknown error occurred.");
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }
}
