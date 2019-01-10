<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}">
        <c:set var="data" value="${document.properties['rcd:data']}" />
        <c:set var="target" value="${data['fichier']}" />
        <c:remove var="index" />
        <c:forEach var="file" items="${document.properties['files:files']}" varStatus="status">
            <c:if test="${target.fileName eq file.file.name and target.digest eq file.file.digest}">
                <c:set var="index" value="${status.index}" />
            </c:if>
        </c:forEach>
    
        <c:if test="${not empty index}">
            <ttc:attachmentLink document="${document}" index="${index}" var="link" />
            
            <li class="media">
                <div class="media-body">
                    <h3 class="h5 media-heading">
                        <a href="${link.url}" target="_blank" class="no-ajax-link">
                            <i class="glyphicons glyphicons-download-alt"></i>
                            <span>${data['_title']}</span>
                            <small>(${data['fichier'].fileName})</small>
                        </a>
                    </h3>
                </div>
            </li>
        </c:if>
    </c:forEach>
</ul>
