<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="erase" var="url"/>


<p class="text-secondary">
    <a href="${url}" class="text-reset">
        <small><op:translate key="FILTER_ERASE"/></small>
    </a>
</p>
