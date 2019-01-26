<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>

<c:set var="share" value="${document.properties['rshr:linkId']}" />

<div class="share">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="halflings halflings-tags"></i>
                <span><op:translate key="PRONOTE" /></span>
            </h3>
        </div>
    
        <div class="panel-body">
                <c:if test="${not empty share}">
                        <p>
                            <span><op:translate key="SHARED_DOC" /></span><br/>
                            <a href="/toutatice-portail-cms-nuxeo/binary?linkId=${share}"><op:translate key="SHARED_LINK" /></a>
                         </p>
                </c:if>
        </div>
    </div>
</div>
