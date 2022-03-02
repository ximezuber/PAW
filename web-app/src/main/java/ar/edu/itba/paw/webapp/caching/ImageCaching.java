package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ImageCaching implements Caching<byte[]> {

    @Override
    public int calculateHash(byte[] element) {
        if(element == null) {
            return 0;
        }
        return Arrays.hashCode(element);
    }
}
