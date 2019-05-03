<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<div class="row">
    <c:forEach var="document" items="${documents}" varStatus="status">
        <div class="col-6 col-md-4 col-lg-3 col-xl-2">
            <div class="card border-light">
                <div class="card-body">
                    <%--Title--%>
                    <div><ttc:title document="${document}" /></div>

                    <c:if test="${not empty document.properties['dc:modified']}">
                        <div>
                            <small class="text-muted">
                                <span><op:translate key=""/></span>
                                <span><op:formatRelativeDate value="${document.properties['dc:modified']}" tooltip="false" /></span>
                            </small>
                        </div>
                    </c:if>
                </div>
            </div>

            <%--Columns break--%>
            <c:choose>
                <c:when test="${status.index % 12 eq 0}">
                    <div class="w-100"></div>
                </c:when>
                <c:when test="${status.index % 6 eq 0}">
                    <div class="w-100 d-lg-none d-xl-block"></div>
                </c:when>
                <c:when test="${status.index % 4 eq 0}">
                    <div class="w-100 d-md-none d-lg-block d-xl-none"></div>
                </c:when>
                <c:when test="${status.index % 3 eq 0}">
                    <div class="w-100 d-none d-md-block d-lg-none"></div>
                </c:when>
                <c:when test="${status.index % 2 eq 0}">
                    <div class="w-100 d-md-none"></div>
                </c:when>
            </c:choose>
        </div>
    </c:forEach>
</div>
