package ar.edu.itba.paw.webapp.helpers;


public class PaginationHelper {
    public static String linkHeaderValueBuilder(String basePath, int currentPage, int maxPage, boolean hasQueryParam) {
        String linkValue = "";
        String path = hasQueryParam? basePath + "&page=": basePath + "page=";
        if(currentPage > 0) {
            path+= (currentPage - 1);
            linkValue += "<" + path + ">;rel=previous";
        }
        if(currentPage < maxPage){
            path+= (currentPage + 1);
            if (!linkValue.equals("")) linkValue += ",";
            linkValue += "<" + path + ">;rel=next";
        }

        return linkValue;
    }
}

