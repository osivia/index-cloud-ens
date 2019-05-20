<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="generate" var="generateUrl" />

<portlet:actionURL name="purge" var="purgeUrl" />

<p>
    <a href="${generateUrl}" class="btn btn-primary no-ajax-link">
        <i class="glyphicons glyphicons-electricity"></i>
        <span><op:translate key="GENERATE" /></span>
    </a>
    
    <a href="${purgeUrl}" class="btn btn-danger no-ajax-link">
        <i class="glyphicons glyphicons-spray"></i>
        <span><op:translate key="PURGE" /></span>
    </a>
</p>
