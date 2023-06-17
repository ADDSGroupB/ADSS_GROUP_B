package BuisnessLayer.TransportManager;

import com.sun.org.apache.bcel.internal.classfile.LocalVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

// This is an abstract class for a truck
public abstract class ATruck {

    // Private variables to store the truck's license plate number, net weight, max cargo weight, status, and truck level
    private final int license_plate_number;
    private final double net_weight;
    private final double max_cargo_weight;
    private TruckStatus status;
    private ArrayList<LocalDate> all_transports;
    private int truck_level;


    // Constructor for the truck class, sets the license plate number, net weight, and max cargo weight, and sets the status to "Available"
    public ATruck(int license_plate_number, double net_weight, double max_cargo_weight) {
        this.license_plate_number = license_plate_number;
        this.net_weight = net_weight;
        this.max_cargo_weight = max_cargo_weight;
        this.status = TruckStatus.Available;
        this.all_transports = new ArrayList<>();
    }

    // Getter method to get the status of the truck
    public TruckStatus getStatus() {
        return status;
    }

    public ArrayList<LocalDate> getAll_transports () { return all_transports;}

    public void addTransportDate (LocalDate date) {all_transports.add(date);}

    public boolean check_if_truck_transporting (LocalDate date) {

        for (LocalDate curr_date : all_transports) {
            if (curr_date.equals(date))
                return true;
        }
        return false;
    }

    public String getType (){
        return "";
    }

    // Setter method to set the status of the truck
    public void setStatus(TruckStatus status) {
        this.status = status;
    }

    // Getter method to get the license plate number of the truck
    public int getLicense_plate_number() {
        return license_plate_number;
    }

    // Getter method to get the net weight of the truck
    public double getNet_weight() {
        return net_weight;
    }

    // Getter method to get the max cargo weight of the truck
    public double getMax_cargo_weight() {
        return max_cargo_weight;
    }

    // Setter method to set the truck level of the truck
    public void setTruck_level(int truck_level) {
        this.truck_level = truck_level;
    }

    // Getter method to get the truck level of the truck
    public int getTruck_level() {
        return truck_level;
    }

    // Overrides the toString method to return a string representation of the truck object
    @Override
    public String toString() {
        return
            "Truck license plate number: " + license_plate_number +
                    ", net weight: " + net_weight +
                    ", max cargo weight: " + max_cargo_weight +
                    ", truck level:" + truck_level +
                    ", truck status: " + status ;
    }

}
