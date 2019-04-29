<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<portlet:actionURL name="drop" var="dropUrl" />

<portlet:resourceURL id="lazy-loading" var="lazyLoadingUrl" />

<c:set var="namespace"><portlet:namespace/></c:set>


<div class="taskbar" data-drop-url="${dropUrl}" data-lazy-loading-url="${lazyLoadingUrl}">
    <ul class="list-unstyled">
        <c:forEach var="task" items="${taskbar.tasks}" varStatus="status">
            <li>
                <div class="d-flex align-items-center">
                    <a href="${task.url}"
                       class="flex-grow-1 py-2 px-3 ${task.active and not task.selected ? 'text-primary font-weight-bold' : 'text-secondary'} text-decoration-none no-ajax-link">
                        <i class="${task.icon}"></i>
                        <span>${task.displayName}</span>
                    </a>

                    <c:if test="${task.expandable}">
                        <small>
                            <a href="#${namespace}-folders-${status.index}"
                               class="d-block text-secondary text-decoration-none no-ajax-link" data-toggle="collapse">
                                <i class="glyphicons glyphicons-halflings-more-vertical"></i>
                            </a>
                        </small>
                    </c:if>
                </div>

                <c:if test="${task.expandable}">
                    <div id="${namespace}-folders-${status.index}" class="collapse ${task.active ? 'show' : ''}">
                        <div class="fancytree mb-3 pl-3">
                            <c:set var="parent" value="${task}" scope="request"/>
                            <jsp:include page="folder-children.jsp"/>
                        </div>
                    </div>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>
