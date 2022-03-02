package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.ClinicDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClinicCaching implements Caching<ClinicDto> {
    @Override
    public int calculateHash(ClinicDto clinic) {
        if(clinic == null) {
            return 0;
        }
        return Objects.hash(clinic.getName(), clinic.getLocation(), clinic.getAddress());
    }
}
