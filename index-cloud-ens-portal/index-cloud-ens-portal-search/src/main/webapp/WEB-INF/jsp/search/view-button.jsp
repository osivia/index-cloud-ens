<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<div class="ml-3">
    <c:set var="title"><op:translate key="SEARCH_OPTIONS"/></c:set>
    <button type="button" class="btn btn-link" data-target="#osivia-modal"
            data-load-url="${optionsUrl}" data-size="large" data-title="${title}">
        <span>${title}</span>
    </button>
</div>
