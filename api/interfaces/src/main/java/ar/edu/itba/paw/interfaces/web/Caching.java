package ar.edu.itba.paw.interfaces.web;

import javax.ws.rs.core.EntityTag;
import java.math.BigInteger;
import java.util.Collection;

public interface Caching<T> {
    int calculateHash(T element);

    default EntityTag calculateEtag(T element) {
        int h = calculateHash(element);
        return new EntityTag(Integer.toString(h));
    }

    default EntityTag calculateEtag(Collection<T> elements) {
        BigInteger hashcode = new BigInteger(String.valueOf(1));
        int elemHash;
        for(T element : elements) {
            elemHash = calculateHash(element);
            hashcode = hashcode.multiply(BigInteger.valueOf(31)).add(BigInteger.valueOf(elemHash));
        }
        return new EntityTag(hashcode.toString());
    }
}
