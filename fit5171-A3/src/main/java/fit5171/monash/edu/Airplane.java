package fit5171.monash.edu;

import java.util.HashMap;
import java.util.Map;

public class Airplane {
    private int airplaneID;
    private String airplaneModel;
    private int businessSitsNumber;
    private int economySitsNumber;
    private int crewSitsNumber;

    // New field: Seat map, rows A–J, each with 7 seats
    private Map<String, Boolean> seatMap;

    public Airplane(int airplaneID, String airplaneModel, int businessSitsNumber, int economySitsNumber, int crewSitsNumber) {
        setAirplaneID(airplaneID);
        setAirplaneModel(airplaneModel);
        setBusinessSitsNumber(businessSitsNumber);
        setEconomySitsNumber(economySitsNumber);
        setCrewSitsNumber(crewSitsNumber);
        initializeSeatMap(); // A–J rows, 7 seats each
    }

    // New method: Initialize seat map
    private void initializeSeatMap() {
        seatMap = new HashMap<>();
        for (char row = 'A'; row <= 'J'; row++) {
            for (int col = 1; col <= 7; col++) {
                seatMap.put(row + String.valueOf(col), false);
            }
        }
    }

    // New getter for seat map
    public Map<String, Boolean> getSeatMap() {
        return seatMap;
    }

    // Existing getters and setters, unchanged
    public int getAirplaneID() {
        return airplaneID;
    }

    public void setAirplaneID(int airplaneID) {
        if (airplaneID <= 0) {
            throw new IllegalArgumentException("Airplane ID must be positive.");
        }
        this.airplaneID = airplaneID;
    }

    public String getAirplaneModel() {
        return airplaneModel;
    }

    public void setAirplaneModel(String airplaneModel) {
        if (airplaneModel == null || airplaneModel.trim().isEmpty()) {
            throw new IllegalArgumentException("Airplane model cannot be null or empty.");
        }
        this.airplaneModel = airplaneModel;
    }

    public int getBusinessSitsNumber() {
        return businessSitsNumber;
    }

    public void setBusinessSitsNumber(int businessSitsNumber) {
        if (businessSitsNumber < 0) {
            throw new IllegalArgumentException("Business seat count cannot be negative.");
        }
        this.businessSitsNumber = businessSitsNumber;
    }

    public int getEconomySitsNumber() {
        return economySitsNumber;
    }

    public void setEconomySitsNumber(int economySitsNumber) {
        if (economySitsNumber < 0) {
            throw new IllegalArgumentException("Economy seat count cannot be negative.");
        }
        this.economySitsNumber = economySitsNumber;
    }

    public int getCrewSitsNumber() {
        return crewSitsNumber;
    }

    public void setCrewSitsNumber(int crewSitsNumber) {
        if (crewSitsNumber < 0) {
            throw new IllegalArgumentException("Crew seat count cannot be negative.");
        }
        this.crewSitsNumber = crewSitsNumber;
    }

    public String toString() {
        return "Airplane{" +
                "model=" + getAirplaneModel() + '\'' +
                ", business sits=" + getBusinessSitsNumber() + '\'' +
                ", economy sits=" + getEconomySitsNumber() + '\'' +
                ", crew sits=" + getCrewSitsNumber() + '\'' +
                '}';
    }

    public static Airplane getAirPlaneInfo(int airplane_id) {
        // TODO Auto-generated method stub
        return null;
    }
}