<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<div class="list-quick-access mb-4">
    <div class="card-deck">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <c:choose>
                <c:when test="${status.count < 2}"><c:set var="display" value="d-flex"/></c:when>
                <c:when test="${status.count < 3}"><c:set var="display" value="d-none d-sm-flex"/></c:when>
                <c:when test="${status.count < 4}"><c:set var="display" value="d-none d-md-flex"/></c:when>
                <c:when test="${status.count < 5}"><c:set var="display" value="d-none d-lg-flex"/></c:when>
                <c:when test="${status.count < 6}"><c:set var="display" value="d-none d-xl-flex"/></c:when>
                <c:otherwise><c:set var="display" value="d-none"/></c:otherwise>
            </c:choose>

            <div class="card ${display}">
                    <%--Image--%>
                <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette"/></c:set>
                <div class="embed-responsive embed-responsive-16by9">
                    <c:choose>
                        <c:when test="${not empty vignetteUrl}">
                            <img src="${vignetteUrl}" alt="" class="card-img-top embed-responsive-item">
                        </c:when>

                        <c:when test="${document.type.name eq 'Picture'}">
                            <c:set var="url"><ttc:documentLink document="${document}" picture="true"
                                                               displayContext="Small"/></c:set>
                            <img src="${url}" alt="" class="card-img-top embed-responsive-item">
                        </c:when>

                        <c:otherwise>
                            <div class="card-img-top embed-responsive-item d-flex align-items-center justify-content-center text-white bg-light">
									<ttc:icon document="${document}" />
							</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                    <%--Body--%>
                <div class="card-body p-3">
                    <div class="text-truncate">
                            <%--Icon--%>
                        <span><ttc:icon document="${document}"/></span>

                            <%--Title--%>
                        <c:set var="url"><ttc:documentLink document="${document}"/></c:set>
                        <a href="${url}" class="stretched-link no-ajax-link">
                            <span><ttc:title document="${document}" linkable="false"/></span>
                        </a>
                    </div>

                    <c:if test="${not empty document.properties['dc:modified']}">
                        <div class="text-truncate">
                            <small class="text-muted">
                                <span><op:translate key="LIST_TEMPLATE_QUICK_ACCESS_MODIFIED_ON"/></span>
                                <span><op:formatRelativeDate value="${document.properties['dc:modified']}"
                                                             tooltip="false"/></span>
                            </small>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</div>