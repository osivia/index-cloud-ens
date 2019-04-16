<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<portlet:renderURL var="sortTitleUrl">
    <portlet:param name="sort" value="title" />
    <portlet:param name="alt" value="${sort eq 'title' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortDateUrl">
    <portlet:param name="sort" value="date" />
    <portlet:param name="alt" value="${sort ne 'date' or not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortLocationUrl">
    <portlet:param name="sort" value="location" />
    <portlet:param name="alt" value="${sort eq 'location' and not alt}"/>
</portlet:renderURL>

<portlet:actionURL name="restore" var="restoreUrl" />
<portlet:actionURL name="empty" var="emptyUrl" />
<portlet:actionURL name="update" var="updateUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>



<c:set var="namespace"><portlet:namespace /></c:set>


<div class="dashboard">
    <form:form action="${updateUrl}" method="post" modelAttribute="dashboardForm" role="form">
        <div class="table" data-location-url="${locationBreadcrumbUrl}">
            <!-- Header -->
            <div class="table-row table-header">
                <!-- Header contextual toolbar -->
                <div class="contextual-toolbar">
                    <nav class="navbar navbar-default">
                    
                        <div class="container-fluid">
                            
                            <!-- Delete selection -->
                            <button type="button" class="btn btn-default navbar-btn" data-toggle="modal" data-target="#${namespace}-delete-selection">
                                <i class="glyphicons glyphicons-bin"></i>
                                <span><op:translate key="DASHBOARD_TOOLBAR_DELETE_SELECTION" /></span>
                            </button>
  
                        </div>
                    </nav>
                </div>
            
                <div class="row">
                    <!-- Document -->
                    <div class="col-sm-6 col-md-4">
                        <a href="${sortTitleUrl}"><op:translate key="DASHBOARD_HEADER_APPLICATION" /></a>
                        
                        <c:if test="${sort eq 'title'}">
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                    <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                </c:choose>
                            </small>
                        </c:if>
                    </div>
                    
                    <!-- Date -->
                    <div class="col-sm-6 col-md-4">
                        <a href="${sortDateUrl}"><op:translate key="DASHBOARD_HEADER_DATE" /></a>
                        
                        <c:if test="${sort eq 'date'}">
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                    <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                </c:choose>
                            </small>
                        </c:if>
                    </div>
                    
                   
                </div>
            </div>
            
            <!-- Body -->
            <div class="popover-container">
                <ul class="list-unstyled selectable">
                    <c:forEach var="application" items="${dashboardForm.applications}" varStatus="status">
                        <li>
                            <div class="table-row">
                                <form:hidden path="applications[${status.index}].selected" />
                            
                                <div class="row">
                                    <!-- Document -->
                                    <div class="col-sm-6 col-md-4">
                                        <div class="form-control-static text-overflow">
                                             <span>${application.token.authentication.clientId}</span>
                                        </div>
                                    </div>
                                    
                                    
                                    <!-- Date -->
            						<c:if test="${not empty application.token.expirationDate}">                                    
	                                    <div class="col-sm-6 col-md-4">
	                                        <div class="text-overflow">
	                                            <span><fmt:formatDate value="${application.token.expirationDate}" type="date" dateStyle="medium" /></span>
	                                        </div>
	                                    </div>
                                    </c:if>
                                 </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        
            <!-- No results -->
            <c:if test="${empty dashboardForm.applications}">
                <div class="table-row">
                    <div class="text-center text-muted"><op:translate key="DASHBOARD_EMPTY_MESSAGE" /></div>
                </div>
            </c:if>
        </div>
        
  
        
        <!-- Delete selection -->
        <div id="${namespace}-delete-selection" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        
                        <h4 class="modal-title">
                            <span><op:translate key="DASHBOARD_DELETE_SELECTION_MODAL_TITLE" /></span>
                        </h4>
                    </div>
                    
                    <div class="modal-body">
                        <p class="text-danger">
                            <span><op:translate key="DASHBOARD_DELETE_SELECTION_MODAL_MESSAGE" /></span>
                        </p>
                        
                    </div>
                    
                    <div class="modal-footer">
                        <button type="submit" name="delete" class="btn btn-danger">
                            <span><op:translate key="DASHBOARD_DELETE_SELECTION" /></span>
                        </button>
                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <span><op:translate key="CANCEL" /></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
    
    
  
</div>
