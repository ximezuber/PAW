package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PatientCaching implements Caching<PatientDto> {

    @Override
    public int calculateHash(PatientDto element) {
        if(element == null) {
            return 0;
        }
        UserDto userData = element.getUserData();
        return Objects.hash(element.getId(), element.getPrepaid(), element.getPrepaidNumber(),
                userData.getEmail(), userData.getFirstName(), userData.getLastName());
    }
}
