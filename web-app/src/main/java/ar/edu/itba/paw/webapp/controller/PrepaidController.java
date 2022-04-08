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
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("prepaid")
public class PrepaidController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private PrepaidService prepaidService;

    @Autowired
    private PrepaidCaching prepaidCaching;

    /**
     * Returns paginated list of prepaid for ADMIN to manage
     * @param page
     * @return list of Prepaid
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getPrepaid(@QueryParam("page") @DefaultValue("0") Integer page,
                                @Context Request request) {
        page = (page < 0) ? 0 : page;
        List<PrepaidDto> prepaid = prepaidService.getPaginatedObjects(page).stream()
                .map(PrepaidDto::fromPrepaid).collect(Collectors.toList());
        int maxPage = prepaidService.maxAvailablePage();

        URI basePath = uriInfo.getAbsolutePathBuilder().build();

        Response.ResponseBuilder response = CacheHelper.handleResponse(prepaid, prepaidCaching,
                new GenericEntity<List<PrepaidDto>>(prepaid) {},
                        "prepaid", request);

        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage);
        if(!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }
        return response.build();
    }

    /**
     * Return list of all prepaid for ADMIN to edit/add clinic.
     * @return list of Prepaid
     */
    @GET
    @Path("/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllPrepaid(@Context Request request) {
        List<PrepaidDto> prepaid = prepaidService.getPrepaids().stream()
                .map(PrepaidDto::fromPrepaid).collect(Collectors.toList());

        return CacheHelper.handleResponse(prepaid, prepaidCaching, new GenericEntity<List<PrepaidDto>>(prepaid) {}, "prepaid", request)
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
