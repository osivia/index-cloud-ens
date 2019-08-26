<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<c:choose>
    <c:when test="${empty form.savedSearches}">
        <p class="text-secondary"><op:translate key="SAVED_SEARCHES_EMPTY"/></p>
    </c:when>

    <c:otherwise>
        <div class="list-group">
            <c:forEach var="savedSearch" items="${form.savedSearches}">
                <portlet:actionURL name="search" var="url">
                    <portlet:param name="id" value="${savedSearch.id}"/>
                </portlet:actionURL>

                <a href="${url}" class="list-group-item list-group-item-action">
                    <span>${savedSearch.displayName}</span>
                </a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
