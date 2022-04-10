package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExistenceServiceImpl implements ExistenceService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PrepaidService prepaidService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private PatientService patientService;


    @Override
    public boolean exists(String input,String type) {
        switch(type){
            case "doctor":
                return doctorService.getDoctorByLicense(input) == null;
            case "user":
                return !userService.userExists(input);
            case "prepaid":
                return prepaidService.getPrepaidByName(input) == null;
            case "specialty":
                return specialtyService.getSpecialtyByName(input) == null;
            case "location":
                return !locationService.getLocationByName(input).isPresent();
            case "patient":
                return patientService.getPatientsById(input).isEmpty();
            default:
                return false;
        }
    }
}
