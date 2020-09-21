<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <%--Brand--%>
    <a href="${requestScope['osivia.home.url']}" class="navbar-brand py-0">
        <img alt="${requestScope['osivia.header.application.name']}" src="${contextPath}/img/logo-cloud-pronote-large.png" height="45">
    </a>


    <div class="collapse navbar-collapse">
        <c:choose>
            <c:when test="${empty requestScope['osivia.toolbar.principal']}">
                <div class="navbar-nav ml-auto">
                    <a href="${requestScope['osivia.toolbar.loginURL']}" class="nav-link text-light">
                        <i class="glyphicons glyphicons-basic-user-rounded h4 mb-0 align-middle"></i>
                        <span><op:translate key="TOOLBAR_LOGIN"/></span>
                    </a>
                </div>
            </c:when>

            <c:otherwise>
                <%--Administration--%>
                <ul class="navbar-nav mr-3">
                    <li class="nav-item">
                        <c:out value="${requestScope['osivia.toolbar.administrationContent']}" escapeXml="false"/>
                    </li>
                </ul>

                <%--Navigation--%>
                <ul class="navbar-nav justify-content-center flex-grow-1 mr-3">
                    <li class="nav-item">
                        <a href="${requestScope['osivia.default.memberPageUrl']}" class="index-tab index-tab-home text-light ${requestScope['osivia.default.memberPage'] ? 'active' : ''}">
                            <i class="glyphicons glyphicons-basic-home"></i>
                            <span class="sr-only"><op:translate key="HOME"/></span>
                        </a>
                    </li>

                    <c:forEach var="navItem" items="${requestScope['osivia.nav.items']}" varStatus="status">
                        <li class="nav-item ml-4">
                            <a href="${navItem.url}" class="index-tab text-${navItem.color} ${empty navItem.url ? 'disabled' : ''} ${navItem.active ? 'active' : ''}">
                                <i class="${navItem.icon}"></i>
                                <span><op:translate key="${navItem.key}"/></span>
                            </a>
                        </li>
                    </c:forEach>
                </ul>

                <ul class="navbar-nav align-items-center">
                    <%--Tasks--%>
                    <li class="nav-item mr-2">
                        <%@ include file="toolbar-tasks.jspf" %>
                    </li>

                    <%--User--%>
                    <li class="nav-item mr-2">
                        <a href="${requestScope['osivia.my-account.url']}" class="nav-link d-flex align-items-center text-light">
                            <c:choose>
                                <c:when test="${empty requestScope['osivia.toolbar.person']}">
                                    <i class="glyphicons glyphicons-basic-user h4 mb-0 mr-2 align-middle"></i>
                                    <span>${requestScope['osivia.toolbar.principal']}</span>
                                </c:when>

                                <c:otherwise>
                                    <img class="avatar" src="${requestScope['osivia.toolbar.person'].avatar.url}" alt="">
                                    <span>${requestScope['osivia.toolbar.person'].cn}</span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>

                    <%--Logout--%>
                    <li class="nav-item">
                        <a href="javascript:" onclick="logout()" class="nav-link text-red">
                            <i class="glyphicons glyphicons-basic-power"></i>
                            <span class="sr-only"><op:translate key="TOOLBAR_LOGOUT"/></span>
                        </a>
                    </li>
                </ul>
            </c:otherwise>
        </c:choose>
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
