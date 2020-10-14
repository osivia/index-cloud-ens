<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:actionURL var="url" name="search"/>

<div class="d-flex justify-content-end">
    <a href="${url}" class="btn btn-link btn-link-hover-primary btn-sm text-secondary text-truncate">
        <i class="glyphicons glyphicons-basic-search"></i>
        <strong><op:translate key="ADVANCED_SEARCH"/></strong>
    </a>
</div>
