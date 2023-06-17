package DataAccessLayer.TransportManager;
import BuisnessLayer.TransportManager.*;
import DataAccessLayer.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class ATruckDAO {

    private Connection conn;
    HashMap<Integer, ATruck> allTrucks;

    public ATruckDAO() throws SQLException {
        allTrucks = new HashMap<>();
    }


    public HashMap<Integer, ATruck> getAllTrucks() throws SQLException {

        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM allTrucks");

            if (rs.isBeforeFirst()) {
                while (rs.next()) {

                    int license_plate_number = rs.getInt("license_plate_number");

                    if (this.allTrucks.containsKey(license_plate_number))
                        continue;

                    double net_weight = rs.getDouble("net_weight");
                    double max_cargo_weight = rs.getDouble("max_cargo_weight");
                    int truck_level = rs.getInt("truck_level");
                    String truck_status = rs.getString("status");
                    ATruck truck = null;

                    switch (truck_level) {
                        case 1:
                            truck = new DryTruck(license_plate_number, net_weight, max_cargo_weight);
                            break;
                        case 2:
                            truck = new CoolerTruck(license_plate_number, net_weight, max_cargo_weight);
                            break;
                        case 3:
                            truck = new FreezerTruck(license_plate_number, net_weight, max_cargo_weight);
                            break;
                    }
                    if (Objects.equals(truck_status, "NotAvailable")) {
                        truck.setStatus(TruckStatus.NotAvailable);
                    }


                    PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM allTransportDocuments WHERE truck_license_plate_number = ?");
                    stmt1.setInt(1, license_plate_number);
                    ResultSet rs1 = stmt1.executeQuery();

                    while (rs1.next()) {
                        truck.addTransportDate(LocalDate.parse(rs1.getString("date")));
                    }
                    this.allTrucks.put(license_plate_number, truck);
                }
                Database.closeConnection(conn);
                return this.allTrucks;
            }
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting all trucks: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public ATruck getATruckById(int id) throws SQLException {

        if (allTrucks.containsKey(id)){
            return allTrucks.get(id);
        }

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM allTrucks WHERE license_plate_number = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int license_plate_number = rs.getInt("license_plate_number");
                double net_weight = rs.getDouble("net_weight");
                double max_cargo_weight = rs.getDouble("max_cargo_weight");
                int truck_level = rs.getInt("truck_level");
                String truck_status = rs.getString("status");
                ATruck truck = null;

                switch (truck_level) {
                    case 1:
                        truck = new DryTruck(license_plate_number, net_weight, max_cargo_weight);
                        break;
                    case 2:
                        truck = new CoolerTruck(license_plate_number, net_weight, max_cargo_weight);
                        break;
                    case 3:
                        truck = new FreezerTruck(license_plate_number, net_weight, max_cargo_weight);
                        break;
                }

                if (Objects.equals(truck_status, "NotAvailable")) {
                    truck.setStatus(TruckStatus.NotAvailable);
                }

                PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM allTransportDocuments WHERE truck_license_plate_number = ?");
                stmt1.setInt(1, license_plate_number);
                ResultSet rs1 = stmt1.executeQuery();

                while (rs1.next()) {
                    truck.addTransportDate(LocalDate.parse(rs1.getString("date")));
                }

                this.allTrucks.put(license_plate_number, truck);
                Database.closeConnection(conn);
                return truck;
            } else {
                Database.closeConnection(conn);
                return null;
            }

        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting a truck by ID: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public void insertATruck(ATruck truck) throws SQLException {

        try {
            conn = Database.connect();
            this.allTrucks.put(truck.getLicense_plate_number(), truck);
            int truck_type = 0;
            if (truck instanceof DryTruck){
                truck_type = 1;
            }
            if (truck instanceof CoolerTruck){
                truck_type = 2;
            }
            if (truck instanceof FreezerTruck){
                truck_type = 3;
            }
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO allTrucks (license_plate_number, net_weight, max_cargo_weight, truck_level, status, truck_type) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, truck.getLicense_plate_number());
            stmt.setDouble(2, truck.getNet_weight());
            stmt.setDouble(3, truck.getMax_cargo_weight());
            stmt.setInt(4, truck.getTruck_level());
            stmt.setString(5, "Available");
            stmt.setInt(6, truck_type);
            stmt.executeUpdate();
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while inserting a truck: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void deleteATruck(int license_plate_number) throws SQLException {

        try {
            conn = Database.connect();
            this.allTrucks.remove(license_plate_number);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM allTrucks WHERE license_plate_number = ?");
            stmt.setInt(1,license_plate_number);
            stmt.executeUpdate();
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting a truck: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void updateATruck(ATruck update_truck) throws SQLException {

        try {
            conn = Database.connect();
            this.allTrucks.put(update_truck.getLicense_plate_number(), update_truck);
            PreparedStatement stmt = conn.prepareStatement("UPDATE allTrucks SET net_weight = ?, max_cargo_weight = ?, truck_level = ?, status = ? WHERE license_plate_number = ?");
            stmt.setDouble(1, update_truck.getNet_weight());
            stmt.setDouble(2, update_truck.getMax_cargo_weight());
            stmt.setInt(3, update_truck.getTruck_level());
            stmt.setString(4, update_truck.getStatus().toString());
            stmt.setInt(5, update_truck.getLicense_plate_number());
            stmt.executeUpdate();
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while updating a truck: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void deleteContent_trucks () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allTrucks");
            Database.closeConnection(conn);
            allTrucks.clear();
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table allTrucks: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

}
