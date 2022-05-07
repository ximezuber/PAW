package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private DoctorService doctorService;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }

        String propertyFrom = permission.toString();

        if(propertyFrom.equals("doctor")) {
            String license = targetDomainObject.toString();
            Optional<Doctor> doctor = doctorService.getDoctorByLicense(license);
            return doctor.get().getEmail().equals(auth.getName());
        } else if(propertyFrom.equals("user")) {
            String email = targetDomainObject.toString();
            return email.equals(auth.getName());
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType) &&
                    grantedAuth.getAuthority().contains(permission)) {
                return true;
            }
        }
        return false;
    }
}
