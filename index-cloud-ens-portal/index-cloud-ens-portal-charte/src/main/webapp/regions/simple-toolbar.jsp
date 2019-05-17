<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<nav class="navbar navbar-expand navbar-dark bg-primary">
    <%--Brand--%>
    <a class="navbar-brand d-none d-md-inline-block py-0" href="${requestScope['osivia.home.url']}">
        <img alt="${requestScope['osivia.header.application.name']}" src="${contextPath}/img/logo-index.png"
             height="40">
    </a>
</nav>
