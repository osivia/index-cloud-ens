<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<div class="d-sm-flex mb-3">
    <div class="flex-grow-1 flex-shrink-1">
        <div class="d-flex">
            <%--Title--%>
            <div class="flex-shrink-1 mr-3">
                <h3 class="h5 text-truncate text-wrap mb-0">
                    <span><ttc:icon document="${document}"/></span>
                    <span><ttc:title document="${document}" linkable="false"/></span>
                </h3>
            </div>

            <%--Size--%>
            <c:if test="${document.type.file}">
                <c:set var="size" value="${document.properties['file:content']['length']}"/>
                <div class="mr-3">
                    <div class="h5 mb-0 text-muted">
                        <small><ttc:fileSize size="${size}"/></small>
                    </div>
                </div>
            </c:if>

            <%--Mutualized indicator--%>
            <c:if test="${not readOnly and document.properties['mtz:enable']}">
                <div class="mr-3">
                    <span class="h5 mb-0 text-mutualized-dark">
                        <i class="glyphicons glyphicons-basic-share"></i>
                    </span>
                </div>
            </c:if>
        </div>
    </div>

    <div>
        <div class="btn-toolbar justify-content-end">
            <%--Mutualize--%>
            <c:if test="${not readOnly and not empty mutualizeUrl}">
                <c:set var="title"><op:translate key="DOCUMENT_FILE_TOOLBAR_MUTUALIZE"/></c:set>
                <a href="javascript:" title="${title}" class="btn btn-mutualized-dark btn-sm mb-1 ml-1 no-ajax-link" data-target="#osivia-modal" data-load-url="${mutualizeUrl}" data-title="${title}">
                    <i class="glyphicons glyphicons-basic-share"></i>
                    <span class="d-none d-xl-inline">${title}</span>
                </a>
            </c:if>

            <%--Copy--%>
            <c:if test="${readOnly and not empty copyUrl}">
                <c:set var="title"><op:translate key="DOCUMENT_FILE_TOOLBAR_COPY"/></c:set>
                <a href="javascript:" title="${title}" class="btn btn-cloud-dark btn-sm mb-1 ml-1 no-ajax-link" data-target="#osivia-modal" data-load-url="${copyUrl}" data-title="${title}">
                    <i class="glyphicons glyphicons-basic-copy-duplicate"></i>
                    <span class="d-none d-xl-inline">${title}</span>
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
                        <c:set var="title"><op:translate key="SHARED_LINK_DEACTIVATE"/></c:set>
                    </c:when>
                    <c:otherwise>
                        <portlet:actionURL name="link-activation" var="activationUrl">
                            <portlet:param name="activate" value="true"/>
                        </portlet:actionURL>
                        <c:set var="icon" value="glyphicons glyphicons-basic-paired" />
                        <c:set var="title"><op:translate key="SHARED_LINK_ACTIVATE"/></c:set>
                    </c:otherwise>
                </c:choose>
                <a href="${activationUrl}" title="${title}" class="btn btn-primary btn-sm mb-1 ml-1">
                    <i class="${icon}"></i>
                    <span class="d-none d-xl-inline">${title}</span>
                </a>
            </c:if>

            <%--Download--%>
            <c:if test="${document.type.file}">
                <c:set var="url"><ttc:documentLink document="${document}" displayContext="download"/></c:set>
                <a href="${url}" target="_blank" title="${title}" class="btn btn-primary btn-sm mb-1 ml-1 no-ajax-link">
                    <i class="glyphicons glyphicons-basic-square-download"></i>
                    <span class="d-none d-xl-inline"><op:translate key="DOWNLOAD"/></span>
                </a>
            </c:if>

            <%--Rename--%>
            <c:if test="${not readOnly and not empty renameUrl}">
                <c:set var="title"><op:translate key="DOCUMENT_RENAME"/></c:set>
                <a href="javascript:" title="${title}" class="btn btn-primary btn-sm mb-1 ml-1 no-ajax-link" data-target="#osivia-modal"
                   data-load-url="${renameUrl}" data-title="${title}">
                    <i class="glyphicons glyphicons-basic-square-edit"></i>
                    <span class="d-none d-xl-inline">${title}</span>
                </a>
            </c:if>

            <%--Edit--%>
            <c:if test="${not readOnly and not empty editUrl}">
                <c:set var="title"><op:translate key="DOCUMENT_EDIT"/></c:set>
                <a href="javascript:" title="${title}" class="btn btn-primary btn-sm mb-1 ml-1 no-ajax-link" data-target="#osivia-modal"
                   data-load-url="${editUrl}" data-title="${title}">
                    <i class="glyphicons glyphicons-basic-import"></i>
                    <span class="d-none d-xl-inline">${title}</span>
                </a>
            </c:if>

            <%--Delete--%>
            <c:if test="${not readOnly and not empty deleteUrl}">
                <c:set var="title"><op:translate key="DELETE"/></c:set>
                <a href="javascript:" title="${title}" class="btn btn-primary btn-sm mb-1 ml-1 no-ajax-link" data-target="#osivia-modal" data-load-url="${deleteUrl}" data-title="${title}">
                    <i class="glyphicons glyphicons-basic-bin"></i>
                    <span class="d-none d-xl-inline">${title}</span>
                </a>
            </c:if>
        </div>
    </div>
</div>
