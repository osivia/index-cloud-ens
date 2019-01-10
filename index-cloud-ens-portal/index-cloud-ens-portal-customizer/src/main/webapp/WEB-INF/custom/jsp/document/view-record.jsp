<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:forEach var="variable" items="${document.properties['rcd:globalVariablesValues']}">
    <c:if test="${variable.name eq 'contenu'}">
        <c:set var="content" value="${variable.value}" />
    </c:if>
</c:forEach>


<div><ttc:transformContent content="${content}"/></div>
