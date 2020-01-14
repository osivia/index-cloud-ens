<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<div class="list-mutualization-space-summary">
    <c:forEach var="document" items="${documents}">
        <div class="card mb-2">
            <div class="card-body">
                <h3 class="card-title h5 mb-1">
                    <%--Icon--%>
                    <span><ttc:icon document="${document}"/></span>

                    <%--Title--%>
                    <c:set var="url"><ttc:documentLink document="${document}"/></c:set>
                    <a href="${url}" class="stretched-link no-ajax-link">
                        <span><ttc:title document="${document}" linkable="false"/></span>
                    </a>
                </h3>

                <c:if test="${not empty document.properties['dc:modified']}">
                    <p class="card-text">
                        <small class="text-muted">
                            <span><op:translate key="LIST_TEMPLATE_QUICK_ACCESS_MODIFIED_ON"/></span>
                            <span><op:formatRelativeDate value="${document.properties['dc:modified']}" tooltip="false"/></span>
                        </small>
                    </p>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>
