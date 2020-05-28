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
            <li class="mb-2">
                <c:choose>
                    <c:when test="${task.add}">
                        <c:if test="${not empty task.dropdownItems}">
                            <div class="dropdown mb-4 ml-3 px-1">
                                <button class="btn btn-blue-light btn-block dropdown-toggle text-truncate no-ajax-link" data-toggle="dropdown"
                                        data-boundary="window">
                                    <i class="glyphicons glyphicons-basic-plus"></i>
                                    <span><op:translate key="TASKBAR_ADD"/></span>
                                </button>

                                <div class="dropdown-menu">
                                    <c:forEach var="dropdownItem" items="${task.dropdownItems}">
                                        <a href="javascript:" class="dropdown-item" data-target="#osivia-modal"
                                           data-load-url="${dropdownItem.url}" data-title="${dropdownItem.modalTitle}">
                                            <c:choose>
                                                <c:when test="${empty dropdownItem.customizedIcon}"><i
                                                        class="${dropdownItem.icon}"></i></c:when>
                                                <c:otherwise>${dropdownItem.customizedIcon}</c:otherwise>
                                            </c:choose>
                                            <span>${dropdownItem.displayName}</span>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                    </c:when>

                    <c:when test="${task.fancytree}">
                        <div class="fancytree overflow-hidden">
                            <ul class="ml-4">
                                <li class="${task.active ? 'current' : (task.searchLocation ? 'search-location' : '')}"
                                    data-retain="${task.selected}"
                                    data-acceptedtypes="${fn:join(task.acceptedTypes, ',')}"
                                    data-expanded="${task.selected}" data-folder="${task.folder}"
                                    data-lazy="${task.lazy}" data-current="${task.active}" data-id="${task.id}"
                                    data-path="${task.path}">
                                    <a href="${task.url}" class="no-ajax-link pl-0">
                                        <span>${task.displayName}</span>
                                    </a>

                                    <!-- Children -->
                                    <c:set var="parent" value="${task}" scope="request"/>
                                    <ttc:include page="folder-children.jsp"/>
                                </li>
                            </ul>
                        </div>
                    </c:when>

                    <c:when test="${task.filtersTitle}">
                        <div class="row mt-4 mb-3 ml-0">
                            <div class="col bg-blue-lighter">
                                <div class="py-1 text-center">
                                    <strong><op:translate key="TASKBAR_FILTERS"/></strong>
                                </div>
                            </div>
                        </div>
                    </c:when>

                    <c:when test="${task.search}">
                        <div class="mb-4 ml-3 px-1">
                            <a href="${task.url}" class="btn btn-blue-light btn-block text-truncate no-ajax-link">
                                <i class="glyphicons glyphicons-basic-search"></i>
                                <span><op:translate key="TASKBAR_SEARCH"/></span>
                            </a>
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
