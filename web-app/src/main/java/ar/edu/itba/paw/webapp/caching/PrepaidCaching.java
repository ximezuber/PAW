package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.PrepaidDto;
import org.springframework.stereotype.Component;

@Component
public class PrepaidCaching implements Caching<PrepaidDto> {

    @Override
    public int calculateHash(PrepaidDto element) {
        if(element == null) {
            return 0;
        }
        return element.getName().hashCode();
    }
}
