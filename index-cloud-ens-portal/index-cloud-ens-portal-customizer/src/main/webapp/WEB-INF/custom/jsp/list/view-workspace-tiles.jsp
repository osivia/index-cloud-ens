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
            
            <div class="col-sm-6 col-lg-4">
                <div class="panel panel-primary">
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
                            <ttc:title document="${document}" />
                        </h3>
                        
                        <!-- Description -->
                        <div class="description-wrapper">
                            <p class="text-pre-wrap">${description}</p>
                        </div>
                        
                        <!-- Member status -->
                        <p class="text-success">
                            <i class="glyphicons glyphicons-ok"></i>
                            <span><op:translate key="LIST_TEMPLATE_STATUS_MEMBER" /></span>
                        </p>
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
