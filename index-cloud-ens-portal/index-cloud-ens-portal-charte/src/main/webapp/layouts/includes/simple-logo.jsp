<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />



<div class="toolbar">
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <h2 class="sr-only">
            <op:translate key="TOOLBAR_TITLE" />
        </h2>

        <div class="container-fluid">
            <div class="navbar-header">
                <div class="visible-xs">
                    <!-- Title -->
                    <div class="clearfix">
                        <p class="navbar-text text-overflow">${requestScope['osivia.header.title']}</p>
                    </div>
                </div>


                <!-- Brand -->
                <a href="${requestScope['osivia.home.url']}" class="navbar-brand hidden-xs">
                    <img alt="${requestScope['osivia.header.application.name']}" src="${contextPath}/img/logo-index.png">
                </a>
            </div>

 
        </div>
    </nav>
</div>

