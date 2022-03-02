package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.DoctorDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DoctorCaching implements Caching<DoctorDto> {
    @Override
    public int calculateHash(DoctorDto doctor) {
        if(doctor == null)
            return 0;
        UserDto userData = doctor.getUser();
        return Objects.hash(doctor.getLicense(), doctor.getPhoneNumber(),
                doctor.getLicense(), userData.getEmail(), userData.getFirstName(),
                userData.getLastName());
    }
}
