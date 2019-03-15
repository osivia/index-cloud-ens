<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page isELIgnored="false"%>


<!-- Levels -->
<c:set var="levels" value="${document.properties['idxcl:levels']}" />
<c:if test="${not empty levels}">
    <dt><op:translate key="DOCUMENT_METADATA_LEVEL" /></dt>
    <dd>
        <ul class="list-unstyled">
            <c:forEach var="level" items="${levels}">
                <li>
                    <span><ttc:vocabularyLabel name="idx_level" key="${level}" /></span>
                </li>
            </c:forEach>
        </ul>
    </dd>
</c:if>


<!-- Subjects -->
<c:set var="subjects" value="${document.properties['idxcl:subjects']}" />
<c:if test="${not empty subjects}">
    <dt><op:translate key="DOCUMENT_METADATA_SUBJECT" /></dt>
    <dd>
        <ul class="list-unstyled">
            <c:forEach var="subject" items="${subjects}">
                <li>
                    <span><ttc:vocabularyLabel name="idx_subject" key="${subject}" /></span>
                </li>
            </c:forEach>
        </ul>
    </dd>
</c:if>
