<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="erase" var="url"/>


<p class="mt-4">
    <a href="${url}" class="btn btn-primary btn-block">
        <span><op:translate key="FILTER_ERASE"/></span>
    </a>
</p>
