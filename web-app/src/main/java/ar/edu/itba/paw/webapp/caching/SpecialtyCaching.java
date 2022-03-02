package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.SpecialtyDto;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyCaching implements Caching<SpecialtyDto> {
    @Override
    public int calculateHash(SpecialtyDto element) {
        if(element == null) {
            return 0;
        }
        return element.getName().hashCode();
    }
}
