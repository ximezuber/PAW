package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationCaching implements Caching<LocationDto> {

    @Override
    public int calculateHash(LocationDto element) {
        if(element == null) {
            return 0;
        }
        return element.getName().hashCode();
    }
}
