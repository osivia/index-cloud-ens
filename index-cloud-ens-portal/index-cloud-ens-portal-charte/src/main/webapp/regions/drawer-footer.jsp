<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<div class="d-md-none">
    <nav class="navbar navbar-expand navbar-light bg-blue-lighter">
        <%--Brand--%>
        <a class="navbar-brand mr-3 py-0" href="#">
            <img alt="Index &Eacute;ducation" src="${contextPath}/img/logo-index.png"
                 height="40">
        </a>

        <ul class="navbar-nav">
            <li class="nav-item">
                <%--RGPD--%>
                <a href="${requestScope['osivia.rgpd.url']}" class="btn btn-blue-dark btn-sm">
                    <span class="text-blue-light font-weight-bolder"><op:translate key="FOOTER_RGPD"/></span>
                    <i class="glyphicons glyphicons-basic-shield-check"></i>
                </a>
            </li>
        </ul>
    </nav>
</div>
