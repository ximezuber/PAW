package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Specialty;

public class SpecialtyDto {

    private String name;

    public static SpecialtyDto fromSpecialty(Specialty specialty) {
        SpecialtyDto dto = new SpecialtyDto();
        dto.name = specialty.getSpecialtyName();
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
