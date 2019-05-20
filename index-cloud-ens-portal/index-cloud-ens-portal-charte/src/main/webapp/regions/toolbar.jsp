<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<nav class="navbar navbar-expand navbar-dark bg-primary">
    <%--Drawer toggle button--%>
    <ul class="navbar-nav d-md-none ml-n1 mr-3 drawer-toggle-button">
        <li class="nav-item">
            <a href="javascript:toggleDrawer()" data-toggle="drawer" class="nav-link">
                <i class="glyphicons glyphicons-basic-menu"></i>
            </a>
        </li>
    </ul>

    <%--Brand--%>
    <a class="navbar-brand d-none d-md-inline-block py-0" href="${requestScope['osivia.home.url']}">
        <img alt="${requestScope['osivia.header.application.name']}" src="${contextPath}/img/logo-index.png"
             height="40">
    </a>

    <%--Administration--%>
    <ul class="navbar-nav d-none d-md-flex">
        <li class="nav-item">
            <c:out value="${requestScope['osivia.toolbar.administrationContent']}" escapeXml="false"/>
        </li>
    </ul>

    <%--Search--%>
    <c:set var="title"><op:translate key="TOOLBAR_SEARCH_TITLE"/></c:set>
    <c:set var="placeholder"><op:translate key="TOOLBAR_SEARCH_PLACEHOLDER"/></c:set>
    <div class="ml-auto">
        <form action="${requestScope['osivia.search.url']}" method="get" class="form-inline flex-nowrap w-auto mr-3 mx-md-4">
            <input type="hidden" name="action" value="advancedSearch">

            <label for="search" class="sr-only">${title}</label>
            <input id="search" type="search" name="search" class="form-control overflow-hidden mr-2" placeholder="${placeholder}">

            <button type="submit" title="${title}" class="btn btn-outline-light" data-toggle="tooltip"
                    data-placement="bottom">
                <i class="glyphicons glyphicons-basic-search"></i>
            </button>
        </form>
    </div>


    <ul class="navbar-nav">
        <c:choose>
            <c:when test="${empty requestScope['osivia.toolbar.principal']}">
                <%--Login--%>
                <li class="nav-item">
                    <a href="${requestScope['osivia.toolbar.loginURL']}" class="nav-link">
                        <i class="glyphicons glyphicons-basic-log-in"></i>
                        <span><op:translate key="TOOLBAR_LOGIN"/></span>
                    </a>
                </li>
            </c:when>

            <c:otherwise>
                <%--Tasks--%>
                <c:if test="${not empty requestScope['osivia.toolbar.tasks.url']}">
                    <c:set var="title"><op:translate key="NOTIFICATION_TASKS"/></c:set>
                    <li class="nav-item mr-2 d-none d-md-block">
                        <a href="javascript:"
                           class="nav-link ${requestScope['osivia.toolbar.tasks.count'] gt 0 ? 'text-warning' : ''}"
                           data-target="#osivia-modal" data-load-url="${requestScope['osivia.toolbar.tasks.url']}"
                           data-load-callback-function="tasksModalCallback" data-title="${title}" data-footer="true">
                            <c:choose>
                                <c:when test="${requestScope['osivia.toolbar.tasks.count'] gt 0}">
                                    <i class="glyphicons glyphicons-basic-bell-ringing"></i>
                                </c:when>

                                <c:otherwise>
                                    <i class="glyphicons glyphicons-basic-bell"></i>
                                </c:otherwise>
                            </c:choose>

                            <span class="sr-only">${title}</span>
                        </a>
                    </li>
                </c:if>

                <%--User menu--%>
                <li class="nav-item dropdown">
                    <a href="javascript:" class="nav-link dropdown-toggle" data-toggle="dropdown">
                        <c:choose>
                            <c:when test="${empty requestScope['osivia.toolbar.person']}">
                                <i class="glyphicons glyphicons-basic-user"></i>
                                <span class="d-none d-lg-inline">${requestScope['osivia.toolbar.principal']}</span>
                            </c:when>

                            <c:otherwise>
                                <img class="avatar" src="${requestScope['osivia.toolbar.person'].avatar.url}" alt="">
                                <span class="d-none d-lg-inline">${requestScope['osivia.toolbar.person'].displayName}</span>
                            </c:otherwise>
                        </c:choose>
                    </a>

                    <div class="dropdown-menu dropdown-menu-right">
                        <div class="dropdown-header d-lg-none">${empty requestScope['osivia.toolbar.person'] ? requestScope['osivia.toolbar.principal'] : requestScope['osivia.toolbar.person'].displayName}</div>

                        <%--User profile--%>
                        <c:set var="url" value="${requestScope['osivia.toolbar.myprofile']}"/>
                        <c:if test="${not empty url}">
                            <a href="${url}" class="dropdown-item">
                                <i class="glyphicons glyphicons-basic-id-badge"></i>
                                <span><op:translate key="TOOLBAR_USER_PROFILE"/></span>
                            </a>
                        </c:if>

                        <%--User settings--%>
                        <%--<c:set var="url" value="${requestScope['osivia.toolbar.userSettings.url']}"/>
                        <c:if test="${not empty url}">
                            <a href="${url}" class="dropdown-item">
                                <i class="glyphicons glyphicons-basic-adjust"></i>
                                <span><op:translate key="TOOLBAR_USER_SETTINGS"/></span>
                            </a>
                        </c:if>--%>

                        <%--User workspace--%>
                        <c:set var="url" value="${requestScope['osivia.userWorkspace.url']}"/>
                        <c:if test="${not empty url}">
                            <a href="${url}" class="dropdown-item">
                                <i class="glyphicons glyphicons-filetypes-folder-user"></i>
                                <span><op:translate key="TOOLBAR_USER_WORKSPACE"/></span>
                            </a>
                        </c:if>

                        <%--Divider--%>
                        <div class="dropdown-divider"></div>

                        <%--Logout--%>
                        <a href="javascript:" onclick="logout()" class="dropdown-item">
                            <i class="glyphicons glyphicons-basic-log-out"></i>
                            <span><op:translate key="TOOLBAR_LOGOUT"/></span>
                        </a>
                    </div>
                </li>
            </c:otherwise>
        </c:choose>
        <li></li>
    </ul>
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
