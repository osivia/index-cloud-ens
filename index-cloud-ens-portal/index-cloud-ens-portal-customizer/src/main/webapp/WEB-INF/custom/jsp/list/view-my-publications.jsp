<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<div class="list-my-publications">
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

                <ul class="card-text list-inline">
                    <%--Views--%>
                    <li class="list-inline-item">
                        <i class="glyphicons glyphicons-basic-eye"></i>
                        <strong>${document.properties['mtz:views']}</strong>
                        <c:choose>
                            <c:when test="${empty document.properties['mtz:views'] or document.properties['mtz:views'] le 1}">
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_VIEW"/></span>
                            </c:when>
                            <c:otherwise>
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_VIEWS"/></span>
                            </c:otherwise>
                        </c:choose>
                    </li>

                    <%--Downloads--%>
                    <li class="list-inline-item">
                        <i class="glyphicons glyphicons-basic-save"></i>
                        <strong>${document.properties['mtz:downloads']}</strong>
                        <c:choose>
                            <c:when test="${empty document.properties['mtz:downloads'] or document.properties['mtz:downloads'] le 1}">
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_DOWNLOAD"/></span>
                            </c:when>
                            <c:otherwise>
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_DOWNLOADS"/></span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>
            </div>
        </div>
    </c:forEach>
</div>
