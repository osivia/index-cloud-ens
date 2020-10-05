<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<div class="d-flex justify-content-between flex-wrap">
    <div class="d-flex mt-1 mb-2">
        <%--Icon--%>
        <div class="flex-shrink-0 mr-2">
            <ttc:icon document="${document}"/>
        </div>

        <%--Title--%>
        <div class="flex-shrink-1 mr-2 text-truncate">
            <strong>${document.title}</strong>
        </div>

        <%--Size--%>
        <c:if test="${document.type.file}">
            <c:set var="size" value="${document.properties['file:content']['length']}"/>
            <div class="flex-shrink-0 mr-2">
                <small class="text-secondary"><ttc:fileSize size="${size}"/></small>
            </div>
        </c:if>

        <%--Mutualized indicator--%>
        <c:if test="${not readOnly and document.properties['mtz:enable']}">
            <div class="flex-shrink-0 mr-2">
                <span class="text-orange">
                    <i class="glyphicons glyphicons-basic-share"></i>
                </span>
            </div>
        </c:if>
    </div>

    <div class="d-flex justify-content-end flex-grow-1 flex-wrap mb-4">
        <%--Mutualize--%>
        <c:if test="${not readOnly and not empty mutualizeUrl}">
            <c:set var="title"><op:translate key="DOCUMENT_FILE_TOOLBAR_MUTUALIZE"/></c:set>
            <a href="javascript:" title="${title}" class="btn btn-link btn-link-hover-orange btn-sm mb-1 ml-2 text-orange-dark no-ajax-link" data-target="#osivia-modal" data-load-url="${mutualizeUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-share"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>

        <%--Copy--%>
        <c:if test="${readOnly and not empty copyUrl}">
            <c:set var="title"><op:translate key="DOCUMENT_FILE_TOOLBAR_COPY"/></c:set>
            <a href="javascript:" title="${title}" class="btn btn-link btn-link-hover-green btn-sm mb-1 ml-2 text-green-dark no-ajax-link" data-target="#osivia-modal" data-load-url="${copyUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-copy-duplicate"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>

        <%--Share--%>
        <c:if test="${not readOnly}">
            <c:choose>
                <c:when test="${document.properties['rshr:enabledLink']}">
                    <portlet:actionURL name="link-activation" var="activationUrl">
                        <portlet:param name="activate" value="false"/>
                    </portlet:actionURL>
                    <c:set var="icon" value="glyphicons glyphicons-basic-paired-off" />
                    <c:set var="title"><op:translate key="DOCUMENT_FILE_TOOLBAR_SHARED_LINK_DEACTIVATE"/></c:set>
                </c:when>
                <c:otherwise>
                    <portlet:actionURL name="link-activation" var="activationUrl">
                        <portlet:param name="activate" value="true"/>
                    </portlet:actionURL>
                    <c:set var="icon" value="glyphicons glyphicons-basic-paired" />
                    <c:set var="title"><op:translate key="DOCUMENT_FILE_TOOLBAR_SHARED_LINK_ACTIVATE"/></c:set>
                </c:otherwise>
            </c:choose>
            <a href="${activationUrl}" title="${title}" class="btn btn-link btn-link-hover-green btn-sm mb-1 ml-2 text-green-dark">
                <i class="${icon}"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>

        <%--Download--%>
        <c:if test="${document.type.file}">
            <c:set var="url"><ttc:documentLink document="${document}" displayContext="download"/></c:set>
            <c:set var="title"><op:translate key="DOWNLOAD"/></c:set>
            <a href="${url}" target="_blank" title="${title}" class="btn btn-link btn-link-hover-green btn-sm mb-1 ml-2 text-green-dark no-ajax-link">
                <i class="glyphicons glyphicons-basic-arrow-thin-down"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>

        <%--Rename--%>
        <c:if test="${not readOnly and not empty renameUrl}">
            <c:set var="title"><op:translate key="DOCUMENT_RENAME"/></c:set>
            <a href="javascript:" title="${title}" class="btn btn-link btn-link-hover-green btn-sm mb-1 ml-2 text-green-dark no-ajax-link" data-target="#osivia-modal"
               data-load-url="${renameUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-square-edit"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>

        <%--Edit--%>
        <c:if test="${not readOnly and not empty editUrl}">
            <c:set var="title"><op:translate key="DOCUMENT_EDIT"/></c:set>
            <a href="javascript:" title="${title}" class="btn btn-link btn-link-hover-green btn-sm mb-1 ml-2 text-green-dark no-ajax-link" data-target="#osivia-modal"
               data-load-url="${editUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-refresh"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>

        <%--Delete--%>
        <c:if test="${not readOnly and not empty deleteUrl}">
            <c:set var="title"><op:translate key="DELETE"/></c:set>
            <a href="javascript:" title="${title}" class="btn btn-link btn-link-hover-green btn-sm mb-1 ml-2 text-green-dark no-ajax-link" data-target="#osivia-modal" data-load-url="${deleteUrl}" data-title="${title}">
                <i class="glyphicons glyphicons-basic-bin"></i>
                <span class="d-none d-md-inline">${title}</span>
            </a>
        </c:if>
    </div>
</div>
