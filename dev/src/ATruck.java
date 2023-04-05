// This is an abstract class for a truck
public abstract class ATruck {

    // Private variables to store the truck's license plate number, net weight, max cargo weight, status, and truck level
    private final int license_plate_number;
    private final int net_weight;
    private final int max_cargo_weight;
    private TruckStatus status;
    private int truck_level;


    // Constructor for the truck class, sets the license plate number, net weight, and max cargo weight, and sets the status to "Available"
    public ATruck(int license_plate_number, int net_weight, int max_cargo_weight) {
        this.license_plate_number = license_plate_number;
        this.net_weight = net_weight;
        this.max_cargo_weight = max_cargo_weight;
        this.status = TruckStatus.Available;
    }

    // Getter method to get the status of the truck
    public TruckStatus getStatus() {
        return status;
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
    public int getNet_weight() {
        return net_weight;
    }

    // Getter method to get the max cargo weight of the truck
    public int getMax_cargo_weight() {
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
        return "Truck: {" +
                "license_plate_number = " + license_plate_number +
                ", net_weight = " + net_weight +
                ", max_cargo_weight = " + max_cargo_weight +
                ", truck_level = " + truck_level +
                ", truck status = " + status +

                '}';
    }
}
