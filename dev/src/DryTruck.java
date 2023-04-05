/**
 * This class represents a dry truck that can transport cargo.
 */
public class DryTruck extends ATruck {

    // The type of truck is always DryTruck
    private final TruckType truck_type = TruckType.DryTruck;

    /**
     * Constructor for creating a new DryTruck object.
     *
     * @param license_plate_number The license plate number of the truck.
     * @param net_weight The net weight of the truck.
     * @param max_cargo_weight The maximum cargo weight that the truck can carry.
     */
    public DryTruck(int license_plate_number, int net_weight, int max_cargo_weight) {
        super(license_plate_number, net_weight, max_cargo_weight);
        this.setTruck_level(1);  // Set the truck level to 1
    }

    /**
     * Returns the type of truck as a string.
     *
     * @return The string "Dry".
     */
    public String GetTruckType() {
        return "Dry";
    }

    /**
     * Returns the type of truck.
     *
     * @return The type of truck (DryTruck).
     */
    public TruckType getTruck_type() {
        return truck_type;
    }

}
