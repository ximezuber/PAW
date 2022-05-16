package ar.edu.itba.paw.webapp.helpers;

import ar.edu.itba.paw.interfaces.web.Caching;

import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheHelper {
    private static final Map<String, Integer> cacheTimes = getTimes();

    private static Map<String, Integer> getTimes() {
        Map<String, Integer> map = new HashMap<>();
        map.put("doctors", 10);
        map.put("doctor", 10);
        map.put("profileImage", 10);
        map.put("doctorsClinics", 10);
        map.put("doctorsClinic", 10);
        map.put("schedules", 10);
        map.put("appointments", 10);
        map.put("clinics", 10);
        map.put("clinic", 10);
        map.put("clinicPrepaid", 10);
        map.put("locations", 10);
        map.put("specialties", 10);
        map.put("prepaid", 10);
        map.put("patient", 10);
        map.put("favorites", 10);
        map.put("favoritesFiltered", 10);

        return map;
    }

    private CacheHelper() {
        throw new UnsupportedOperationException();
    }

    private static CacheControl getCacheControl(String key) {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(cacheTimes.get(key));
        return cc;
    }

    private static <T> EntityTag getTag(T cacheable, Caching<T> service) {
        return service.calculateEtag(cacheable);
    }

    private static <T> EntityTag getTag(List<T> cacheable, Caching<T> service) {
        return service.calculateEtag(cacheable);
    }

    private static EntityTag verifyTag(EntityTag etag, Request request) {
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);

        //si etag no matchea, builder es null y hay que mandar entidad
        return builder == null ? etag : null;
    }

    private static <T> EntityTag evaluateTag(T cacheable, Caching<T> service, Request request) {
        EntityTag etag = getTag(cacheable, service);
        return verifyTag(etag, request);
    }

    private static <T> EntityTag evaluateTag(List<T> cacheable, Caching<T> service, Request request) {
        EntityTag etag = getTag(cacheable, service);
        return verifyTag(etag, request);
    }

    private static Response.ResponseBuilder handleCache(EntityTag etag, CacheControl cc) {
        //si llega nulo es porque no se modifico
        if (etag == null) {
            return Response.status(Response.Status.NOT_MODIFIED).cacheControl(cc);
        }
        //tag no matchea o no hay header if none match
        return null;
    }

    public static <T> Response.ResponseBuilder handleResponse(T cacheable, Caching<T> service, String key, Request request) {
        return getResponseBuilder(key, evaluateTag(cacheable, service, request), Response.ok(cacheable));
    }

    public static <T> Response.ResponseBuilder handleResponse(List<T> cacheable, Caching<T> service, GenericEntity<List<T>> token, String key, Request request) {
        return getResponseBuilder(key, evaluateTag(cacheable, service, request), Response.ok(token));
    }

    private static <T> Response.ResponseBuilder getResponseBuilder(String key, EntityTag entityTag, Response.ResponseBuilder ok) {
        CacheControl cc = getCacheControl(key);
        Response.ResponseBuilder isValid = handleCache(entityTag, cc);

        if(isValid != null) {
            //el objeto no se modifico, devuelvo Response.Status.NOT_MODIFIED
            return isValid;
        }

        return ok.cacheControl(cc).tag(entityTag);
    }
}
