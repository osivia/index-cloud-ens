<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<div class="workspace-tiles">
    <div class="row">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
            <c:set var="description" value="${document.properties['dc:description']}" />
            <c:set var="workspaceType" value="${document.properties['workspaceType']}" />
            <c:set var="memberStatus" value="${document.properties['memberStatus']}" />
            
            <portlet:actionURL name="createRequest" var="createRequestUrl">
                <portlet:param name="id" value="${document.properties['webc:url']}" />
            </portlet:actionURL>
            
            
            <div class="col-sm-6 col-lg-4">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <!-- Vignette -->
                        <div class="thumbnail">
                            <c:choose>
                                <c:when test="${empty vignetteUrl}">
                                    <div class="empty">
                                        <i class="glyphicons glyphicons-picture"></i>
                                    </div>
                                </c:when>
                                
                                <c:otherwise>
                                    <img src="${vignetteUrl}" alt="" class="text-middle">
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                
                    <div class="panel-body">
                        <!-- Title -->
                        <h3 class="h4 text-center">
                            <ttc:title document="${document}" linkable="${(workspaceType.id eq 'PUBLIC')}" />
                        </h3>
                        
                        <!-- Type -->
                        <c:if test="${not empty workspaceType}">
                            <p class="text-center">
                                <span class="label label-${workspaceType.color}">
                                    <i class="${workspaceType.icon}"></i>
                                    <span><op:translate key="LIST_TEMPLATE_${workspaceType.key}" /></span>
                                </span>
                            </p>
                        </c:if>
                        
                        <!-- Description -->
                        <div class="description-wrapper">
                            <p class="text-pre-wrap">${description}</p>
                        </div>
                        
                        <!-- Action -->
                        <c:choose>
                            <c:when test="${empty memberStatus}">
                                <div class="text-right">
                                    <a href="${createRequestUrl}" class="btn btn-default btn-sm">
                                        <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CREATION" /></span>
                                    </a>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                <p class="text-${memberStatus.color}">
                                    <i class="${memberStatus.icon}"></i>
                                    <span><op:translate key="${memberStatus.key}" /></span>
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Responsive columns reset -->
            <c:choose>
                <c:when test="${status.count % 6 eq 0}"><div class="clearfix"></div></c:when>
                <c:when test="${status.count % 3 eq 0}"><div class="clearfix visible-lg-block"></div></c:when>
                <c:when test="${status.count % 2 eq 0}"><div class="clearfix visible-sm-block visible-md-block"></div></c:when>
            </c:choose>
       </c:forEach>
    </div>
</div>


<c:if test="${empty documents}">
    <p class="text-center">
        <span class="text-muted"><op:translate key="LIST_NO_ITEMS" /></span>
    </p>
</c:if>
