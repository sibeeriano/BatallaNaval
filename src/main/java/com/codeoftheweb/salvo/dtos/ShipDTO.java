package com.codeoftheweb.salvo.dtos;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.models.ShipType;

import java.util.List;

public class ShipDTO {

    private ShipType type;

    private List<String> locations;

    public ShipDTO() {
    }

    public ShipDTO(Ship ship){
        this.type = ship.getType();
        this.locations = ship.getLocations();
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}