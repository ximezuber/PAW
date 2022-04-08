package ar.edu.itba.paw.webapp.helpers;


import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class PaginationHelper {
    public static String linkHeaderValueBuilder(URI basePath, int currentPage, int maxPage) {
        String linkValue = "";
        if(currentPage > 0) {
            URI prevPath = UriBuilder.fromUri(basePath).queryParam("page", currentPage - 1).build();
            linkValue += "<" + prevPath + ">;rel=previous";
        }
        if(currentPage < maxPage - 1) {
            URI nextPath = UriBuilder.fromUri(basePath).queryParam("page", currentPage + 1).build();
            if (!linkValue.equals("")) linkValue += ",";
            linkValue += "<" + nextPath + ">;rel=next";
        }

        return linkValue;
    }
}

