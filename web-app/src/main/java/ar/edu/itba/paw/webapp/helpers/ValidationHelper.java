package ar.edu.itba.paw.webapp.helpers;

import ar.edu.itba.paw.interfaces.service.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class ValidationHelper {
    private ValidationHelper() {
        throw new UnsupportedOperationException();
    }


    public static boolean appointmentValidate(String doctorLicense, String patientEmail, LocalDateTime date, AppointmentService appointmentService){
       return appointmentService.hasAppointment(doctorLicense,patientEmail,date);
    }

    public static boolean photoValidate(MultipartFile photo){
       if(!photo.isEmpty()){
           String contentType = photo.getContentType();
           return !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg"));
       }
       return false;
    }
}
