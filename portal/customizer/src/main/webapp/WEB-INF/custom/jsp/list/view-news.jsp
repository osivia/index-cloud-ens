<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}">
        <ttc:documentLink document="${document}" var="link" />
        <c:set var="data" value="${document.properties['rcd:data']}" />
    
        <li class="media">
            <div class="media-body">
                <h3 class="h5 media-heading">
                    <a href="${link.url}" class="no-ajax-link">${data['_title']}</a>
                </h3>
                <div><ttc:transformContent content="${data['contenu']}" /></div>
            </div>
        </li>
    </c:forEach>
</ul>
