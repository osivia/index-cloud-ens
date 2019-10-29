<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:actionURL var="searchFiltersUrl" name="search-filters"/>


<a href="${searchFiltersUrl}" class="btn btn-primary btn-sm">
    <i class="glyphicons glyphicons-basic-search"></i>
    <span class="d-none d-sm-inline"><op:translate key="SEARCH_FILTERS_DISPLAY"/></span>
</a>
