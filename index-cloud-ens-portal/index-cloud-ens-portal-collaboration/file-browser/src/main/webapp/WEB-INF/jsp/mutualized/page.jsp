<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page isELIgnored="false" %>

<c:forEach var="item" items="${items}">
    <%@ include file="table-row.jspf" %>
</c:forEach>

<c:if test="${nextPageIndex gt 0}">
    <portlet:actionURL name="save-position" var="savePositionUrl">
        <portlet:param name="page" value="${nextPageIndex}"/>
    </portlet:actionURL>
    <portlet:resourceURL id="load-page" var="loadPageUrl">
        <portlet:param name="page" value="${nextPageIndex}"/>
    </portlet:resourceURL>


    <div class="portal-table-row">
        <div class="portal-table-cell">
            <div class="portal-table-cell-inner">
                <button type="button" class="btn btn-primary" data-save-position-url="${savePositionUrl}" data-load-page-url="${loadPageUrl}">
                    <span class="spinner-border spinner-border-sm" role="status"></span>
                    <span>Loading...</span>
                </button>
            </div>
        </div>
    </div>
</c:if>
