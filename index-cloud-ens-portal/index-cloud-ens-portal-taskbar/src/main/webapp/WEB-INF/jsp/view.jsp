<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<portlet:actionURL name="drop" var="dropUrl"/>

<portlet:resourceURL id="lazy-loading" var="lazyLoadingUrl"/>


<c:set var="namespace"><portlet:namespace/></c:set>

<c:set var="select2Searching"><op:translate key="SELECT2_SEARCHING"/></c:set>
<c:set var="select2NoResults"><op:translate key="SELECT2_NO_RESULTS"/></c:set>


<div class="taskbar" data-drop-url="${dropUrl}" data-lazy-loading-url="${lazyLoadingUrl}">
    <ul class="list-unstyled">
        <c:forEach var="task" items="${taskbar.tasks}" varStatus="status">
            <li>
                <c:choose>
                    <%--Add--%>
                    <c:when test="${task.add}">
                        <c:if test="${not empty task.dropdownItems}">
                            <div class="dropdown mb-4">
                                <button class="dropdown-toggle btn btn-green btn-block rounded-pill text-white text-truncate no-ajax-link"
                                        data-toggle="dropdown"
                                        data-boundary="window">
                                    <i class="glyphicons glyphicons-basic-plus"></i>
                                    <strong><op:translate key="TASKBAR_ADD"/></strong>
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

                    <%--Navigation tree--%>
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

                    <%--Filters--%>
                    <c:when test="${task.filters}">
                        <hr class="mt-5">

                        <div class="mb-3">
                            <a href="#${namespace}-filters"
                               class="d-flex align-items-center text-secondary text-decoration-none no-ajax-link"
                               data-toggle="collapse">
                                <small class="flex-shrink-0">
                                    <i class="glyphicons glyphicons-basic-set-down"></i>
                                </small>

                                <strong class="flex-grow-1 text-black text-center text-truncate"><op:translate
                                        key="TASKBAR_FILTERS"/></strong>
                            </a>

                            <c:choose>
                                <c:when test="${empty task.savedSearches}">
                                    <p class="text-secondary"><op:translate key="TASKBAR_SAVED_SEARCHES_EMPTY"/></p>
                                </c:when>

                                <c:otherwise>
                                    <div id="${namespace}-filters" class="collapse show ml-4">
                                        <c:forEach var="savedSearch" items="${task.savedSearches}">
                                            <div class="card card-custom card-custom-hover card-custom-gray my-3">
                                                <div class="card-body">
                                                    <div class="text-truncate">
                                                        <a href="${savedSearch.url}"
                                                           class="stretched-link ${savedSearch.active ? 'text-green' : 'text-black'} text-decoration-none no-ajax-link">
                                                            <strong>${savedSearch.displayName}</strong>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:when>

                    <%--Search--%>
                    <c:when test="${task.search}">
                        <hr class="mt-5">

                        <div class="mb-3">
                            <a href="#${namespace}-search"
                               class="d-flex align-items-center text-secondary text-decoration-none no-ajax-link"
                               data-toggle="collapse">
                                <small class="flex-shrink-0">
                                    <i class="glyphicons glyphicons-basic-set-down"></i>
                                </small>

                                <strong class="flex-grow-1 text-black text-center text-truncate"><op:translate
                                        key="TASKBAR_SEARCH"/></strong>
                            </a>

                            <div id="${namespace}-search" class="collapse show">
                                <%--Reset--%>
                                <portlet:actionURL var="resetUrl" name="reset"/>
                                <div class="d-flex justify-content-end my-3">
                                    <a href="${resetUrl}"
                                       class="btn btn-link btn-link-hover-green btn-sm text-secondary text-truncate">
                                        <i class="glyphicons glyphicons-basic-reload"></i>
                                        <strong><op:translate key="TASKBAR_RESET_SEARCH"/></strong>
                                    </a>
                                </div>

                                <%--@elvariable id="searchForm" type="fr.index.cloud.ens.taskbar.portlet.model.TaskbarSearchForm"--%>
                                <form:form action="${submitUrl}" method="post" modelAttribute="searchForm">
                                    <%--Keywords--%>
                                    <c:set var="placeholder"><op:translate key="TASKBAR_SEARCH_KEYWORDS"/></c:set>
                                    <div class="form-group">
                                        <form:label path="keywords" cssClass="sr-only">${placeholder}</form:label>
                                        <div class="input-group">
                                            <form:input path="keywords" cssClass="form-control" placeholder="${placeholder}"/>
                                            <div class="input-group-append">
                                                <span class="input-group-text">
                                                    <i class="glyphicons glyphicons-basic-search"></i>
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <%--Document type--%>
                                    <portlet:resourceURL id="load-vocabulary" var="loadUrl">
                                        <portlet:param name="vocabulary" value="idx_document_type"/>
                                    </portlet:resourceURL>
                                    <c:set var="placeholder"><op:translate key="TASKBAR_SEARCH_DOCUMENT_TYPE"/></c:set>
                                    <div class="form-group">
                                        <form:label path="documentType" cssClass="sr-only">${placeholder}</form:label>
                                        <form:select path="documentType" cssClass="form-control select2 select2-default" data-placeholder="${placeholder}" data-url="${loadUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                                            <c:forEach var="item" items="${searchForm.documentType}">
                                                <form:option value="${item}">${item}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>

                                    <%--Level--%>
                                    <portlet:resourceURL id="load-vocabulary" var="loadUrl">
                                        <portlet:param name="vocabulary" value="idx_level"/>
                                    </portlet:resourceURL>
                                    <c:set var="placeholder"><op:translate key="TASKBAR_SEARCH_LEVEL"/></c:set>
                                    <div class="form-group">
                                        <form:label path="level" cssClass="sr-only">${placeholder}</form:label>
                                        <form:select path="level" cssClass="form-control select2 select2-default" data-placeholder="${placeholder}" data-url="${loadUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                                            <c:forEach var="item" items="${searchForm.level}">
                                                <form:option value="${item}">${item}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>

                                    <%--Subject--%>
                                    <portlet:resourceURL id="load-vocabulary" var="loadUrl">
                                        <portlet:param name="vocabulary" value="idx_subject"/>
                                    </portlet:resourceURL>
                                    <c:set var="placeholder"><op:translate key="TASKBAR_SEARCH_SUBJECT"/></c:set>
                                    <div class="form-group">
                                        <form:label path="subject" cssClass="sr-only">${placeholder}</form:label>
                                        <form:select path="subject" cssClass="form-control select2 select2-default" data-placeholder="${placeholder}" data-url="${loadUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                                            <c:forEach var="item" items="${searchForm.subject}">
                                                <form:option value="${item}">${item}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </form:form>

                                <%--Advanced search--%>
                                <c:if test="${not empty task.advancedSearch}">
                                    <div class="d-flex justify-content-end">
                                        <a href="${task.advancedSearch.url}"
                                           class="btn btn-link btn-link-hover-green btn-sm text-secondary text-truncate no-ajax-link">
                                            <i class="glyphicons glyphicons-basic-search"></i>
                                            <strong><op:translate key="TASKBAR_ADVANCED_SEARCH"/></strong>
                                        </a>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <a href="${task.url}"
                           class="d-flex align-items-center mb-3 ml-4 ${task.active ? 'text-green' : 'text-secondary'} text-decoration-none text-truncate no-ajax-link">
                            <i class="${task.icon}"></i>
                            <strong class="ml-2 text-black">${task.displayName}</strong>
                        </a>
                    </c:otherwise>
                </c:choose>
            </li>
        </c:forEach>
    </ul>
</div>
