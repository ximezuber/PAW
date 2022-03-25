package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.PrepaidService;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.webapp.caching.PrepaidCaching;
import ar.edu.itba.paw.webapp.dto.PrepaidDto;
import ar.edu.itba.paw.webapp.form.PrepaidForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("prepaids")
public class PrepaidController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private PrepaidService prepaidService;

    @Autowired
    private PrepaidCaching prepaidCaching;

    /**
     * Returns paginated list of prepaids for ADMIN to manage
     * @param page
     * @return list of Prepaids
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getPrepaids(@QueryParam("page") @DefaultValue("0") Integer page,
                                @Context Request request) {
        page = (page < 0) ? 0 : page;
        List<PrepaidDto> prepaids = prepaidService.getPaginatedObjects(page).stream()
                .map(PrepaidDto::fromPrepaid).collect(Collectors.toList());
        int maxPage = prepaidService.maxAvailablePage();

        String basePath = "/api/prepaids?";

        Response.ResponseBuilder response = CacheHelper.handleResponse(prepaids, prepaidCaching,
                new GenericEntity<List<PrepaidDto>>(prepaids) {},
                        "prepaids", request);

        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage, false);
        if(!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }
        return response.build();
    }

    /**
     * Return list of all prepaids for ADMIN to edit/add clinic.
     * @return list of Prepaids
     */
    @GET
    @Path("/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllPrepaids(@Context Request request) {
        List<PrepaidDto> prepaids = prepaidService.getPrepaids().stream()
                .map(PrepaidDto::fromPrepaid).collect(Collectors.toList());

        return CacheHelper.handleResponse(prepaids, prepaidCaching, new GenericEntity<List<PrepaidDto>>(prepaids) {}, "prepaids", request)
                .build();
    }

    /**
     * Lets ADMIN delete a prepaid
     * @param name
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("/{name}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response deleteLocation(@PathParam("name") final String name) throws EntityNotFoundException {
        prepaidService.deletePrepaid(name);
        return Response.noContent().build();
    }

    /**
     * Lets ADMIN add prepaid to application
     * @param form
     * @return
     * @throws DuplicateEntityException
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPrepaid(@Valid PrepaidForm form) throws DuplicateEntityException {
        Prepaid prepaid = prepaidService.createPrepaid(form.getName());
        return Response.created(uriInfo.getAbsolutePathBuilder()
                .path(prepaid.getName()).build()).build();
    }
}
