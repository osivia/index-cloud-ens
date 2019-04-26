<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
 
<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="drop" var="dropUrl" />

<portlet:resourceURL id="lazy-loading" var="lazyLoadingUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>


<div class="taskbar portlet-filler hidden-scrollbar" data-drop-url="${dropUrl}" data-lazy-loading-url="${lazyLoadingUrl}">
    <!-- Folders -->
    <ul class="folders">
        <c:forEach var="folder" items="${taskbar.folders}">
            <li ${folder.active ? 'class="active"' : ''}>
                <a href="${folder.url}" class="no-ajax-link">
                    <span>${folder.displayName}</span>
                </a>
                
                <!-- Children -->
                <c:if test="${folder.active and not empty folder.children}">
                    <div class="fancytree">
                        <c:set var="parent" value="${folder}" scope="request" />
                        <ttc:include page="folder-children.jsp" />
                    </div>
                </c:if>
            </li>
        </c:forEach>
    </ul>
    
    <!-- Services -->
    <ul class="services">
        <c:forEach var="service" items="${taskbar.services}">
            <li ${service.active ? 'class="active"' : ''}>
                <a href="${service.url}" class="no-ajax-link" data-type="${service.type}">
                    <i class="${service.icon}"></i>
                    <span>${service.displayName}</span>
                </a>
            </li>
        </c:forEach>
    </ul>
    
    <!-- Administration -->
    <c:if test="${not empty taskbar.administration}">
        <hr>
    
        <ul class="administration">
            <li>
                <a href="#${namespace}-administration" class="no-ajax-link" data-toggle="collapse">
                    <i class="glyphicons glyphicons-cogwheel"></i>
                    <span><op:translate key="WORKSPACE_TASKBAR_ADMINISTRATION" /></span>
                    <span class="caret"></span>
                </a>
            </li>
        </ul>
        
        <ul id="${namespace}-administration" class="administration collapse">
            <c:forEach var="service" items="${taskbar.administration}">
                <li>
                    <a href="${service.url}" class="no-ajax-link" data-type="administration">
                        <i class="${service.icon}"></i>
                        <span>${service.displayName}</span>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</div>
