<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:set var="author" value="${document.properties['dc:creator']}" />
<c:set var="lastContributor" value="${document.properties['dc:lastContributor']}" />
<c:set var="created" value="${document.properties['dc:created']}" />
<c:set var="modified" value="${document.properties['dc:modified']}" />
<c:set var="publicationDate" value="${document.properties['ttc:publicationDate']}" />


<div class="metadata">
    <div class="panel panel-default">
        <div class="panel-body">
            <!-- Levels -->
            <c:set var="levels" value="${document.properties['idxcl:levels']}" />
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_LEVEL_PLACEHOLDER" /></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="idxcl:levels" />
            </portlet:actionURL>
            <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                <portlet:param name="vocabulary" value="idx_level" />
                <portlet:param name="optgroupDisabled" value="true" />
            </portlet:resourceURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition">
                    <label for="${namespace}-levels" class="control-label"><op:translate key="DOCUMENT_METADATA_LEVEL_LABEL" /></label>
                    <select id="${namespace}-levels" name="inline-values" multiple="multiple" class="form-control select2 select2-inline-edition" data-url="${select2Url}" data-placeholder="${placeholder}">
                        <c:forEach var="level" items="${levels}">
                            <option value="${level}" selected="selected"><ttc:vocabularyLabel name="idx_level" key="${level}" /></option>
                        </c:forEach>
                    </select>
                </div>
                
                <input type="submit" class="hidden">
            </form>
            
            
            <!-- Subjects -->
            <c:set var="subjects" value="${document.properties['idxcl:subjects']}" />
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_SUBJECT_PLACEHOLDER" /></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="idxcl:subjects" />
            </portlet:actionURL>
            <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                <portlet:param name="vocabulary" value="idx_subject" />
                <portlet:param name="optgroupDisabled" value="true" />
            </portlet:resourceURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition">
                    <label for="${namespace}-subjects" class="control-label"><op:translate key="DOCUMENT_METADATA_SUBJECT_LABEL" /></label>
                    <select id="${namespace}-subjects" name="inline-values" multiple="multiple" class="form-control select2 select2-inline-edition" data-url="${select2Url}" data-placeholder="${placeholder}">
                        <c:forEach var="subject" items="${subjects}">
                            <option value="${subject}" selected="selected"><ttc:vocabularyLabel name="idx_subject" key="${subject}" /></option>
                        </c:forEach>
                    </select>
                </div>
                
                <input type="submit" class="hidden">
            </form>
            
            
            <!-- Description -->
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_DESCRIPTION_PLACEHOLDER" /></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="dc:description" />
            </portlet:actionURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition">
                    <label for="${namespace}-description" class="control-label"><op:translate key="DOCUMENT_METADATA_DESCRIPTION_LABEL" /></label>
                    <textarea id="${namespace}-description" name="inline-values" rows="3" cols="" placeholder="${placeholder}" class="form-control">${document.properties['dc:description']}</textarea>
                </div>
                
                <input type="submit" class="hidden">
            </form>
            
            
            <!-- Pronote share & targets -->
            <c:set var="share" value="${document.properties['rshr:linkId']}" />
            <c:set var="targets" value="${document.properties['rshr:targets']}" />
            <c:if test="${not empty share and not empty targets}">
                <c:if test="${not empty share}">
                    <p>
                        <span><op:translate key="SHARED_DOC" /></span><br/>
                        <a href="/s/${share}"><op:translate key="SHARED_LINK" /></a>
                     </p>
                 </c:if>
                
                <c:if test="${not empty targets}">
                    <dt><op:translate key="SHARED_TARGET" /></dt>
                    <c:forEach var="target" items="${targets}">
                        <dd>
                            <span>${target.pubTitle}</span>
                        </dd>
                    </c:forEach>
                </c:if>
            </c:if>
        
        
            <!-- Remote publication spaces -->
            <dl class="no-margin">
                <ttc:include page="metadata-remote-sections.jsp" />
            </dl>
        </div>
    </div>
</div>
