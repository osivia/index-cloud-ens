<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<div class="list-quick-access">
    <div class="card-deck">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <c:choose>
                <c:when test="${status.count < 2}"><c:set var="display" value="d-flex"/></c:when>
                <c:when test="${status.count < 3}"><c:set var="display" value="d-none d-sm-flex"/></c:when>
                <c:when test="${status.count < 4}"><c:set var="display" value="d-none d-md-flex"/></c:when>
                <c:when test="${status.count < 5}"><c:set var="display" value="d-none d-xl-flex"/></c:when>
                <c:otherwise><c:set var="display" value="d-none"/></c:otherwise>
            </c:choose>

            <div class="card card-custom card-custom-border-bottom card-custom-${document.properties['mtz:enable'] ? 'orange' : 'green'} ${display} shadow">
                <div class="card-body">
                    <%--Badges--%>
                    <div class="card-custom-badges">
                        <%--PRONOTE indicator--%>
                        <c:if test="${not empty document.properties['rshr:targets']}">
                            <img src="/index-cloud-ens-charte/img/pronote-indicator.png" alt="PRONOTE" height="15">
                        </c:if>

                        <%--Mutualized document--%>
                        <c:if test="${document.properties['mtz:enable']}">
                            <i class="glyphicons glyphicons-basic-share text-orange"></i>
                        </c:if>
                    </div>

                    <%--Icon--%>
                    <div class="card-custom-icon card-custom-icon-lg mb-3">
                        <ttc:icon document="${document}" />
                    </div>

                    <%--Title--%>
                    <h3 class="card-title mb-0 text-truncate">
                        <c:set var="url"><ttc:documentLink document="${document}"/></c:set>
                        <a href="${url}" title="${document.title}" class="stretched-link text-black text-decoration-none no-ajax-link">
                            <span>${document.title}</span>
                        </a>
                    </h3>

                    <c:if test="${not empty document.properties['dc:modified']}">
                        <div class="text-truncate">
                            <small class="text-muted">
                                <span><op:translate key="LIST_TEMPLATE_QUICK_ACCESS_MODIFIED_ON"/></span>
                                <span><op:formatRelativeDate value="${document.properties['dc:modified']}" tooltip="false"/></span>
                            </small>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
