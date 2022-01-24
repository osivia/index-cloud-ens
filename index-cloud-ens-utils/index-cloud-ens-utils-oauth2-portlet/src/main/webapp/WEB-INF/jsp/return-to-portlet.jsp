<%@ page contentType="text/html" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<c:choose>
    <c:when test="${empty originUrl}">
        originUrl is null.
    </c:when>
    <c:otherwise>
	    <script>
	          document.location = '${originUrl}';
	    </script>
    </c:otherwise>
</c:choose>
