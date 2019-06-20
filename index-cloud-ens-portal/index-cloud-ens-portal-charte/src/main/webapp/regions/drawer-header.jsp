<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<div class="d-md-none">
    <nav class="navbar navbar-expand navbar-light">
        <%--Brand--%>
        <a class="navbar-brand mx-auto py-0" href="${requestScope['osivia.home.url']}">
            <img alt="${requestScope['osivia.header.application.name']}" src="${contextPath}/img/logo-cloud-pronote.png"
                 height="40">
        </a>
    </nav>


    <c:if test="${not empty requestScope['osivia.nav.items']}">
        <nav class="navbar navbar-expand navbar-dark bg-blue-light">
            <ul class="navbar-nav flex-column">
                <c:forEach var="navItem" items="${requestScope['osivia.nav.items']}">
                    <li class="nav-item ${navItem.active ? 'active' : ''}">
                        <a href="${navItem.url}" class="nav-link small">
                            <i class="${navItem.icon}"></i>
                            <strong><op:translate key="${navItem.key}"/></strong>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
</div>
