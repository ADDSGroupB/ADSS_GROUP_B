package BuisnessLayer.TransportManager;

/**
 * This class represents a Cooler Truck, which is a type of truck that extends from the ATruck class.
 * It has a truck type of Cooler Truck and a truck level of 2.
 */
public class CoolerTruck extends ATruck {

    private final TruckType truck_type = TruckType.CoolerTruck;

    /**
     * Creates a CoolerTruck object with the given license plate number, net weight, and maximum cargo weight.
     * It also sets the truck level to 2.
     *
     * @param license_plate_number the license plate number of the truck
     * @param net_weight the net weight of the truck
     * @param max_cargo_weight the maximum cargo weight the truck can carry
     */
    public CoolerTruck(int license_plate_number, double net_weight, double max_cargo_weight ) {
        super(license_plate_number, net_weight, max_cargo_weight);
        this.setTruck_level(2);
    }

    /**
     * Returns the truck type of Cooler Truck.
     *
     * @return the truck type of Cooler Truck
     */
    public TruckType getTruck_type() {
        return truck_type;
    }

    /**
     * Returns the string representation of the truck type, which is "Cooler".
     *
     * @return the string representation of the truck type
     */
    public String getType () {
        return "Cooler";
    }

    // Overrides the toString method to return a string representation of the truck object
    @Override
    public String toString() {
        return
                "Truck license plate number: " + getLicense_plate_number() +
                ", Type: Cooler " +
                ", net weight: " + getNet_weight() +
                ", max cargo weight: " + getMax_cargo_weight() +
                ", truck level:" + getTruck_level() +
                ", truck status: " + getStatus() ;
    }


}