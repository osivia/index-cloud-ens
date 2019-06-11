<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<div class="highest-levels">
    <c:choose>
        <c:when test="${empty highestLevels.items}">
            <p class="text-muted text-center"><op:translate key="HIGHEST_LEVELS_EMPTY"/></p>
        </c:when>

        <c:otherwise>
            <div class="list-group">
                <c:forEach var="item" items="${highestLevels.items}">
                    <portlet:actionURL name="search" var="url">
                        <portlet:param name="id" value="${item.id}"/>
                    </portlet:actionURL>

                    <a href="${url}" class="list-group-item list-group-item-action">
                        <span>${item.label}</span>
                        <span class="badge badge-secondary">${item.count}</span>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
