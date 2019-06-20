<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<nav class="navbar navbar-expand navbar-light bg-blue-lighter d-none d-md-flex">
    <%--Brand--%>
    <a class="navbar-brand mr-5 py-0" href="#">
        <img alt="Index &Eacute;ducation" src="${contextPath}/img/logo-index-full.png"
             height="40">
    </a>

    <ul class="navbar-nav">
        <li class="nav-item">
            <%--RGPD--%>
            <a href="${requestScope['osivia.rgpd.url']}" class="btn btn-blue-dark">
                <span class="text-blue-light font-weight-bolder"><op:translate key="FOOTER_RGPD"/></span>
                <i class="glyphicons glyphicons-basic-shield-check"></i>
            </a>
        </li>
    </ul>
</nav>
