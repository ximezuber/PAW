package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class AppointmentCaching implements Caching<AppointmentDto> {
    @Override
    public int calculateHash(AppointmentDto element) {
        if(element == null) {
            return 0;
        }
        URI doctor = element.getDoctor();
        LocalDateTime date = LocalDateTime.of(
                element.getYear(),
                element.getMonth(),
                element.getDay(),
                element.getHour(),
                0);
        URI clinic = element.getClinic();
        URI patient = element.getPatient();
        return Objects.hash(date, doctor, clinic, patient);
    }
}
