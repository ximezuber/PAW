package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LocationService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.webapp.caching.LocationCaching;
import ar.edu.itba.paw.webapp.dto.LocationDto;
import ar.edu.itba.paw.webapp.form.LocationForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @Autowired
    LocationCaching locationCaching;

    @Autowired
    MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    /**
     * Returns paginated list of locations for ADMIN to manage
     * @param page
     * @return list of Locations
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getLocations(@QueryParam("page") @DefaultValue("0") Integer page,
                                 @Context Request request) {

        page = (page < 0) ? 0 : page;

        List<LocationDto> locations = locationService.getPaginatedObjects(page).stream()
                .map(LocationDto::fromLocation).collect(Collectors.toList());
        int maxPage = locationService.maxAvailablePage() - 1;

        URI basePath = uriInfo.getAbsolutePathBuilder().build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage);

        Response.ResponseBuilder response = CacheHelper.handleResponse(locations, locationCaching,
                new GenericEntity<List<LocationDto>>(locations) {}, "locations", request);

        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }
        return response.build();
    }

    /**
     * Returns list of all locations for ADMIN to add/edit a clinic
     * @return list of Locations
     */
    @GET
    @Path("/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllLocations(@Context Request request) {
        List<LocationDto> locations = locationService.getLocations().stream()
                .map(LocationDto::fromLocation).collect(Collectors.toList());

        Response.ResponseBuilder ret = CacheHelper.handleResponse(locations, locationCaching,
                        new GenericEntity<List<LocationDto>>(locations) {}, "locations", request);
        return ret.build();
    }

    /**
     * Lets ADMIN delete location. If a there is a clinic on the location to be deleted, it throws
     * EntityDependencyException.
     * @param name
     * @return
     * @throws EntityNotFoundException
     * @throws EntityDependencyException
     */
    @DELETE
    @Path("/{name}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response deleteLocation(@PathParam("name") final String name)
            throws EntityNotFoundException, EntityDependencyException {
        locationService.deleteLocation(name);
        return Response.noContent().build();
    }

    /**
     * Lets ADMIN add location to application
     * @param form
     * @return
     * @throws DuplicateEntityException
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLocation(@Valid final LocationForm form) throws DuplicateEntityException {
        Location location = locationService.createLocation(form.getName());
        return Response.created(uriInfo.getAbsolutePathBuilder().
                path(location.getLocationName()).build()).build();
    }
}
