package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import ar.edu.itba.paw.webapp.dto.DoctorClinicDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class AppointmentCaching implements Caching<AppointmentDto> {
    @Override
    public int calculateHash(AppointmentDto element) {
        if(element == null) {
            return 0;
        }
        DoctorClinicDto doctorClinic = element.getDoctorClinic();
        LocalDateTime date = element.getDate();
        return Objects.hash(date, doctorClinic.getDoctor().getLicense());
    }
}
