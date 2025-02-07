import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double calculatePrice(int rentalDays) { return basePricePerDay * rentalDays; }
    public boolean isAvailable() { return isAvailable; }
    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }
    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public int getDays() { return days; }
}

class CarRentalSystemGUI {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    private JFrame frame;
    private JTextArea displayArea;

    public CarRentalSystemGUI() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();

        frame = new JFrame("Car Rental System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        JButton rentButton = new JButton("Rent a Car");
        JButton returnButton = new JButton("Return a Car");
        JButton exitButton = new JButton("Exit");
        displayArea = new JTextArea();
        displayArea.setEditable(false);

        rentButton.addActionListener(e -> rentCarGUI());
        returnButton.addActionListener(e -> returnCarGUI());
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(rentButton);
        panel.add(returnButton);
        panel.add(exitButton);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void addCar(Car car) { cars.add(car); }
    public void addCustomer(Customer customer) { customers.add(customer); }

    private void rentCarGUI() {
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name == null || name.trim().isEmpty()) return;

        String carList = "Available Cars:\n";
        for (Car car : cars) {
            if (car.isAvailable()) {
                carList += car.getCarId() + " - " + car.getBrand() + " " + car.getModel() + "\n";
            }
        }

        String carId = JOptionPane.showInputDialog(carList + "Enter Car ID to rent:");
        String daysStr = JOptionPane.showInputDialog("Enter rental days:");
        int days = Integer.parseInt(daysStr);

        Customer customer = new Customer("CUS" + (customers.size() + 1), name);
        addCustomer(customer);

        for (Car car : cars) {
            if (car.getCarId().equals(carId) && car.isAvailable()) {
                double price = car.calculatePrice(days);
                car.rent();
                rentals.add(new Rental(car, customer, days));
                displayArea.append("\nCar rented: " + car.getBrand() + " " + car.getModel() + " for " + days + " days.\nTotal price: $" + price + "\n");
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Car not available.");
    }

    private void returnCarGUI() {
        String carId = JOptionPane.showInputDialog("Enter Car ID to return:");
        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equals(carId)) {
                rental.getCar().returnCar();
                rentals.remove(rental);
                displayArea.append("\nCar " + carId + " returned successfully.\n");
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Invalid Car ID or Car not rented.");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystemGUI rentalSystem = new CarRentalSystemGUI();

        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
    }
}
