package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Prepaid;

public class PrepaidDto {
    String name;

    public static PrepaidDto fromPrepaid(Prepaid prepaid) {
        PrepaidDto dto = new PrepaidDto();
        dto.name = prepaid.getName();
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
