<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<nav class="navbar navbar-expand-md navbar-light border-bottom mb-2">
    <%--Drawer toggle button--%>
    <ul class="navbar-nav d-md-none ml-n1 mr-3 drawer-toggle-button">
        <li class="nav-item">
            <a href="javascript:toggleDrawer()" class="nav-link" data-toggle="drawer">
                <i class="glyphicons glyphicons-basic-menu"></i>
            </a>
        </li>
    </ul>

    <%--Brand--%>
    <a href="${requestScope['osivia.home.url']}" class="navbar-brand py-0">
        <img alt="${requestScope['osivia.header.application.name']}"
             src="${contextPath}/img/logo-cloud-pronote-large.png" class="my-n2" height="32">
    </a>
    

    

    <ul class="navbar-nav d-md-none flex-row">
        <%--Tasks--%>
        <li class="nav-item">
            <%@include file="toolbar-tasks.jspf" %>
        </li>

        <%--Navbar toggler--%>
        <li class="nav-item ml-3">
            <button type="button" class="btn btn-outline-secondary" data-toggle="collapse" data-target="#navbar">
                <span><op:translate key="NAVBAR_TOGGLER"/></span>
                <i class="glyphicons glyphicons-basic-set-down"></i>
            </button>
        </li>
    </ul>

    <div id="navbar" class="collapse navbar-collapse">
        <%--Administration--%>
        <ul class="navbar-nav d-none d-md-flex mr-3">
            <li class="nav-item">
                <c:out value="${requestScope['osivia.toolbar.administrationContent']}" escapeXml="false"/>
            </li>
        </ul>

        <ul class="navbar-nav flex-sm-row justify-content-center align-items-center mt-sm-2 mt-md-0 mr-md-auto">
        
        
 
            
	        <li class="nav-item mr-3">
	            <a href="${requestScope['osivia.default.memberPageUrl']}" class="index-home ${requestScope['osivia.default.memberPage'] ? 'active' : ''}">
	                <i class="glyphicons glyphicons-basic-home"></i>
	                <span class="sr-only"><op:translate key="HOME"/></span>
	            </a>
	        </li>
        
            <c:forEach var="navItem" items="${requestScope['osivia.nav.items']}" varStatus="status">
                <li class="nav-item ${status.first ? '' : 'mt-2 mt-sm-0'} ${status.last ? '' : 'mr-sm-3'}">
                    <a href="${navItem.url}"
                       class="index-tab index-tab-${navItem.color} ${empty navItem.url ? 'disabled' : ''} ${navItem.active ? 'active' : ''}">
                        <i class="${navItem.icon}"></i>
                        <strong class="text-uppercase"><op:translate key="${navItem.key}"/></strong>
                    </a>
                </li>
            </c:forEach>
        </ul>

        <ul class="navbar-nav">
            <c:choose>
                <c:when test="${empty requestScope['osivia.toolbar.principal']}">
                    <%--Login--%>
                    <li class="nav-item mt-2 mt-md-0">
                        <a href="${requestScope['osivia.toolbar.loginURL']}" class="nav-link">
                            <i class="glyphicons glyphicons-basic-log-in"></i>
                            <span class="d-md-none d-lg-inline"><op:translate key="TOOLBAR_LOGIN"/></span>
                        </a>
                    </li>
                </c:when>

                <c:otherwise>
                    <%--Tasks--%>
                    <li class="nav-item d-none d-md-block">
                        <%@ include file="toolbar-tasks.jspf" %>
                    </li>

                    <%--User menu (=< sm)--%>
                    <c:set var="url" value="${requestScope['osivia.my-account.url']}"/>
                    <c:if test="${not empty url}">
                        <li class="nav-item mt-2 mt-md-0 dropdown d-md-none">
                            <a href="${url}" class="nav-link">
                                <i class="glyphicons glyphicons-basic-id-badge"></i>
                                <span><op:translate key="TOOLBAR_USER_ACCOUNT"/></span>
                            </a>
                        </li>
                    </c:if>
                    <%--User menu (> sm)--%>
                    <li class="nav-item mt-2 mt-md-0 dropdown d-none d-md-block">
                        <a href="javascript:" class="nav-link dropdown-toggle" data-toggle="dropdown">
                            <c:choose>
                                <c:when test="${empty requestScope['osivia.toolbar.person']}">
                                    <i class="glyphicons glyphicons-basic-user"></i>
                                    <span class="d-md-none d-lg-inline">${requestScope['osivia.toolbar.principal']}</span>
                                </c:when>

                                <c:otherwise>
                                    <img class="avatar" src="${requestScope['osivia.toolbar.person'].avatar.url}"
                                         alt="">
                                    <span class="d-md-none d-lg-inline">${requestScope['osivia.toolbar.person'].cn}</span>
                                </c:otherwise>
                            </c:choose>
                        </a>

                        <div class="dropdown-menu dropdown-menu-right">
                            <div class="dropdown-header d-lg-none">${empty requestScope['osivia.toolbar.person'] ? requestScope['osivia.toolbar.principal'] : requestScope['osivia.toolbar.person'].cn}</div>

                                <%--User account--%>
                            <c:if test="${not empty url}">
                                <a href="${url}" class="dropdown-item">
                                    <i class="glyphicons glyphicons-basic-id-badge"></i>
                                    <span><op:translate key="TOOLBAR_USER_ACCOUNT"/></span>
                                </a>
                            </c:if>
                        </div>
                    </li>


                    <%--Logout--%>
                    <li class="nav-item mt-2 mt-md-0">
                        <a href="javascript:" onclick="logout()" class="nav-link">
                            <i class="glyphicons glyphicons-basic-log-out"></i>
                            <span class="d-md-none d-lg-inline"><op:translate key="TOOLBAR_LOGOUT"/></span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>


<%--Disconnection modal--%>
<div id="disconnection" class="modal fade" data-apps="${op:join(requestScope['osivia.sso.applications'], '|')}"
     data-redirection="${requestScope['osivia.toolbar.signOutURL']}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <i class="glyphicons glyphicons-basic-door"></i>
                <span><op:translate key="TOOLBAR_LOGOUT_MESSAGE"/></span>
            </div>
        </div>
    </div>

    <div class="apps-container d-none"></div>
</div>
