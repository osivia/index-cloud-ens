<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<c:choose>
    <c:when test="${empty form.savedSearches}">
        <p class="text-secondary"><op:translate key="SAVED_SEARCHES_EMPTY"/></p>
    </c:when>

    <c:otherwise>
        <c:forEach var="savedSearch" items="${form.savedSearches}">
            <portlet:actionURL name="search" var="url">
                <portlet:param name="id" value="${savedSearch.id}"/>
            </portlet:actionURL>

            <div class="card card-custom card-custom-hover card-custom-gray mb-3">
                <div class="card-body py-3">
                    <div class="text-truncate">
                        <a href="${url}" title="${savedSearch.displayName}" class="stretched-link text-black text-decoration-none">
                            <strong>${savedSearch.displayName}</strong>
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>
