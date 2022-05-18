package ar.edu.itba.paw.webapp.helpers;

import ar.edu.itba.paw.interfaces.web.Caching;

import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheHelper {
    private static final Map<String, Integer> cacheTimes = getTimes();

    private static Map<String, Integer> getTimes() {
        //seconds
        Map<String, Integer> map = new HashMap<>();
        map.put("doctors", 86400); // 24hrs
        map.put("doctor", 86400);
        map.put("profileImage", 86400);
        map.put("doctorsClinics", 86400);
        map.put("doctorsClinic", 86400);
        map.put("schedules", 86400);
        map.put("appointments", 1800); // 30 min
        map.put("clinics", 86400);
        map.put("clinic", 86400);
        map.put("clinicPrepaid", 86400);
        map.put("locations", 86400);
        map.put("specialties", 86400);
        map.put("prepaid", 86400);
        map.put("patient", 86400);
        map.put("favorites", 1800);
        map.put("favoritesFiltered", 1800);

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
