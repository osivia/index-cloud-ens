<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<portlet:actionURL name="drop" var="dropUrl"/>

<portlet:resourceURL id="lazy-loading" var="lazyLoadingUrl"/>

<c:set var="namespace"><portlet:namespace/></c:set>


<div class="taskbar" data-drop-url="${dropUrl}" data-lazy-loading-url="${lazyLoadingUrl}">
    <ul class="list-unstyled">
        <c:forEach var="task" items="${taskbar.tasks}" varStatus="status">
            <li>
                <c:choose>
                    <c:when test="${task.fancytree}">
                        <div class="fancytree overflow-hidden">
                            <ul>
                                <li class="${task.active ? 'current' : ''}"
                                    data-retain="${task.selected}"
                                    data-acceptedtypes="${fn:join(task.acceptedTypes, ',')}"
                                    data-expanded="${task.selected}" data-folder="${task.folder}"
                                    data-lazy="${task.lazy}" data-current="${task.active}" data-id="${task.id}"
                                    data-path="${task.path}">
                                    <a href="${task.url}" class="no-ajax-link">
                                        <span>${task.displayName}</span>
                                    </a>

                                    <!-- Children -->
                                    <c:set var="parent" value="${task}" scope="request"/>
                                    <ttc:include page="folder-children.jsp"/>
                                </li>
                            </ul>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <a href="${task.url}"
                           class="d-block ${task.active ? 'text-primary font-weight-bold' : 'text-secondary'} text-decoration-none text-truncate no-ajax-link">
                            <i class="${task.icon}"></i>
                            <span>${task.displayName}</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </li>
        </c:forEach>
    </ul>
</div>
