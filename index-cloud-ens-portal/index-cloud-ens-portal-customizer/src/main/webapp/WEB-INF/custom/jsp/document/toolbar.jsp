<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<div class="d-flex align-items-center mb-3">
    <%--Title--%>
    <div class="mr-3">
        <h3 class="h5 mb-0">
            <span><ttc:icon document="${document}"/></span>
            <span><ttc:title document="${document}" linkable="false"/></span>
        </h3>
    </div>

    <%--Size--%>
    <c:if test="${document.type.file}">
        <c:set var="size" value="${document.properties['file:content']['length']}"/>
        <div class="mr-3">
            <span class="text-muted"><ttc:fileSize size="${size}"/></span>
        </div>
    </c:if>

    <%--Mutualized indicator--%>
    <c:if test="${not empty document.publishedDocuments}">
        <div class="mr-3">
            <span class="h5 mb-0 text-mutualized-dark">
                <i class="glyphicons glyphicons-basic-share"></i>
            </span>
        </div>
    </c:if>

    <div class="flex-grow-1 text-right">
        <%--Mutualize--%>
        <portlet:actionURL name="mutualize" var="mutualizeUrl"/>
        <a href="${mutualizeUrl}" class="btn btn-mutualized-dark btn-sm">
            <i class="glyphicons glyphicons-basic-share"></i>
            <span><op:translate key="DOCUMENT_FILE_TOOLBAR_MUTUALIZE"/></span>
        </a>

        <%--Download--%>
        <c:if test="${document.type.file}">
            <c:choose>
                <c:when test="${document.type.name eq 'Picture'}">
                    <c:set var="url"><ttc:documentLink document="${document}" picture="true"/></c:set>
                </c:when>
                <c:otherwise>
                    <c:set var="url"><ttc:documentLink document="${document}" displayContext="download"/></c:set>
                </c:otherwise>
            </c:choose>
            <a href="${url}" target="_blank" class="btn btn-primary btn-sm no-ajax-link">
                <i class="glyphicons glyphicons-basic-square-download"></i>
                <span><op:translate key="DOWNLOAD"/></span>
            </a>
        </c:if>

        <%--Rename--%>
        <c:if test="${not empty renameUrl}">
            <c:set var="title"><op:translate key="DOCUMENT_RENAME"/></c:set>
            <a href="javascript:" class="btn btn-primary btn-sm no-ajax-link" data-target="#osivia-modal"
               data-load-url="${renameUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-square-edit"></i>
                <span>${title}</span>
            </a>
        </c:if>

        <%--Edit--%>
        <c:if test="${not empty editUrl}">
            <c:set var="title"><op:translate key="DOCUMENT_EDIT"/></c:set>
            <a href="javascript:" class="btn btn-primary btn-sm no-ajax-link" data-target="#osivia-modal"
               data-load-url="${editUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-import"></i>
                <span>${title}</span>
            </a>
        </c:if>

        <%--Delete--%>
        <c:if test="${not empty deleteUrl}">
            <c:set var="title"><op:translate key="DELETE"/></c:set>
            <a href="javascript:" class="btn btn-primary btn-sm no-ajax-link" data-target="#osivia-modal" data-load-url="${deleteUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-bin"></i>
                <span>${title}</span>
            </a>
        </c:if>
    </div>
</div>
