<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<ul class="nav navbar-nav">
    <!-- Home -->
    <li role="presentation">
        <a href="${displayItem.url}">
            <i class="glyphicons glyphicons-home"></i>
            <span class="visible-xs-inline">${displayItem.title}</span>
        </a>
    </li>

    <c:forEach var="page" items="${displayItem.children}">
        <c:choose>
            <c:when test="${empty page.children}">
                <li role="presentation">
                    <a href="${page.url}" target="${page.external ? '_blank' : ''}">
                        <span>${page.title}</span>
                        <c:if test="${page.external}">
                            <i class="glyphicons glyphicons-new-window-alt"></i>
                        </c:if>
                    </a>
                </li>
            </c:when>
            
            <c:otherwise>
                <li class="dropdown" role="presentation">
                    <a href="${page.url}" class="dropdown-toggle" data-toggle="dropdown">
                        <span>${page.title}</span>
                        <span class="caret"></span>
                    </a>
                    
                    <ul class="dropdown-menu" role="menu">
                        <c:forEach var="subPage" items="${page.children}">
                            <li>
                                <a href="${subPage.url}" target="${subPage.external ? '_blank' : ''}">
                                    <span>${subPage.title}</span>
                                    <c:if test="${subPage.external}">
                                        <i class="glyphicons glyphicons-new-window-alt"></i>
                                    </c:if>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</ul>
