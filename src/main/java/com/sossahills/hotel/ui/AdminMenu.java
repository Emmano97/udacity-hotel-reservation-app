package com.sossahills.hotel.ui;

import com.sossahills.hotel.api.AdminResource;
import com.sossahills.hotel.model.*;

import java.util.*;

public class AdminMenu {

    private final Scanner scanner;
    private final AdminResource adminResource;
    
    public AdminMenu(AdminResource adminResource, Scanner scanner) {
        this.adminResource = adminResource;
        this.scanner = scanner;
    }


    public void printMenu() {
        System.out.println("");
        System.out.println("Admin menu of SOSSA Hills Hotel Reservation App");
        System.out.println("Select a menu option");
        System.out.println("----------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Back to Main Menu");
        System.out.println("----------------------------------------");
    }

    public void displayCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers == null || customers.isEmpty()) {
            System.out.println("There is no customer");
            return;
        }else for (Customer customer: customers) System.out.println(customer);
    }

    public void showAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms == null || rooms.isEmpty()) System.out.println("There is no room!");
        else {
            for (IRoom room: rooms) System.out.println(room);
        }
    }

    public void displayReservations() {
        Set<Reservation> reservations = adminResource.getAllReservations();
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("There is no reservation");
            return;
        }else for (Reservation reservation: reservations) System.out.println(reservation);
    }

    public void addRoom() {
        List<IRoom> newRooms = new ArrayList<>();
        boolean isAdding = true;
        while (isAdding) {
            String roomNumber = getRoomNumber(newRooms);
            double roomPrice = getRoomPrice();
            RoomType roomType = getRoomType();
            newRooms.add(new Room(roomNumber, roomPrice, roomType));
            isAdding = getRoom();
        }
        adminResource.addRoom(newRooms);
        System.out.println("Rooms successfully added");
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

    private String getRoomNumber(List<IRoom> newRooms) {
        System.out.println("Enter room number");
        String input = "";
        boolean isNotRoomNumber = true;
        while (isNotRoomNumber) {
            input = scanner.nextLine();
            if (!isNumber(input)) {
                System.out.println("Invalid room number");
                continue;
            }
            if (! isNewRoomNumber(newRooms, input))System.out.println("This room number exits");
            else isNotRoomNumber = false;
        }
        return input;
    }

    private boolean isNewRoomNumber(List<IRoom> rooms, String roomNumber) {
        for (IRoom room: rooms) if (room.getRoomNumber().equals(roomNumber)) return false;
        return true;
    }

    private double getRoomPrice() {
        System.out.println("Enter room price");
        boolean isBadRoomPrice = true;
        String input = "";
        while (isBadRoomPrice) {
            input = scanner.nextLine();
            if (! isNumber(input)) {
                System.out.println("Room price should be a decimal number");
                continue;
            }
            isBadRoomPrice = false;
        }
        return Double.parseDouble(input);
    }

    private RoomType getRoomType() {
        System.out.println("Choose room type. \"s\" for single or " +
                "\"d\" for double");
        RoomType roomType = null;
        boolean isBadRoomType = true;
        while (isBadRoomType) {
            String input = scanner.nextLine();
            switch (input) {
                case "d", "D" -> {
                    isBadRoomType = false;
                    roomType = RoomType.DOUBLE;
                }
                case "s", "S" -> {
                    isBadRoomType = false;
                    roomType = RoomType.SINGLE;
                }
                default -> System.out.println("Enter \"s\" for single or \"d\" " +
                        "for double");
            }
        }
        return roomType;
    }

    private boolean getRoom() {
        System.out.println("Add another room? (y/n)");
        boolean keepAddingRooms = true;
        boolean isBadInput = true;
        while (isBadInput) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y" ->
                        isBadInput = false;
                case "n" -> {
                    isBadInput = false;
                    keepAddingRooms = false;
                }
                default -> // Keep inside inner loop
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return keepAddingRooms;
    }
    
    
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                this.printMenu();
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1 -> this.displayCustomers();
                    case 2 -> this.showAllRooms();
                    case 3 -> this.displayReservations();
                    case 4 -> this.addRoom();
                    case 5 -> {
                        System.out.println("Returning to the main menu");
                        keepRunning = false;
                    }
                    default -> System.out.println("Please enter a number representing a menu option from above");
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
