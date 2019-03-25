<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page isELIgnored="false"%>


<c:set var="namespace"><portlet:namespace /></c:set>


<!-- Levels -->
<c:set var="levels" value="${document.properties['idxcl:levels']}" />
<portlet:actionURL name="inline-edition" var="submitUrl">
    <portlet:param name="property" value="idxcl:levels" />
</portlet:actionURL>
<portlet:resourceURL id="select2-vocabulary" var="select2Url">
    <portlet:param name="vocabulary" value="idx_level" />
</portlet:resourceURL>
<form action="${submitUrl}" method="post">
    <div class="form-group inline-edition">
        <label for="${namespace}-levels" class="control-label"><op:translate key="DOCUMENT_METADATA_LEVEL" /></label>
        <select id="${namespace}-levels" name="inline-values" multiple="multiple" class="form-control select2 select2-inline-edition" data-url="${select2Url}" data-placeholder="Test de placeholder">
            <c:forEach var="level" items="${levels}">
                <option value="${level}" selected="selected"><ttc:vocabularyLabel name="idx_level" key="${level}" /></option>
            </c:forEach>
        </select>
    </div>
    
    <input type="submit" class="hidden">
</form>


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
