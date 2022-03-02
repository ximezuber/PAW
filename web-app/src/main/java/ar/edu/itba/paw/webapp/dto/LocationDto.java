package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Location;

public class LocationDto {
    private String name;

    public static LocationDto fromLocation(Location location) {
        LocationDto dto = new LocationDto();
        dto.name = location.getLocationName();
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}