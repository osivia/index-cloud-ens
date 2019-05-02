<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<portlet:actionURL name="cancel-inline-edition" var="cancelUrl" />


<div class="metadata">
    <div class="panel panel-default">
        <div class="panel-body">
            <!-- Levels -->
            <c:set var="levels" value="${document.properties['idxcl:levels']}" />
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_LEVEL_PLACEHOLDER" /></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="idxcl:levels" />
                <portlet:param name="cancel-url" value="${cancelUrl}" />
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
                <portlet:param name="cancel-url" value="${cancelUrl}" />
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
                <portlet:param name="cancel-url" value="${cancelUrl}" />
            </portlet:actionURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition">
                    <label for="${namespace}-description" class="control-label"><op:translate key="DOCUMENT_METADATA_DESCRIPTION_LABEL" /></label>
                    <textarea id="${namespace}-description" name="inline-values" rows="3" cols="" placeholder="${placeholder}" class="form-control">${document.properties['dc:description']}</textarea>
                </div>
                
                <input type="submit" class="hidden">
            </form>
            
       </div>
	</div>
     <!-- Pronote share & targets -->
     <c:set var="share" value="${document.properties['rshr:linkId']}" />
     <c:set var="targets" value="${document.properties['rshr:targets']}" />	
	 <c:if test="${not empty share}">	
		<div class="panel panel-default">
	        <div class="panel-body">	     
                    <p>
                        <strong><op:translate key="SHARED_LINK" /></strong><br/>
                        <a id="${namespace}-link-url" class="btn btn-link  text-overflow" href="/s/${share}">https://${pageContext.request.serverName}/s/${share}</a>
                     </p>
					<!-- Format -->
		            <portlet:actionURL name="inline-edition" var="submitUrl">
		                <portlet:param name="property" value="rshr:format" />
		                <portlet:param name="cancel-url" value="${cancelUrl}" />
		            </portlet:actionURL>
		            <form action="${submitUrl}" method="post">
		                <div class="form-group inline-edition">
		                    <label class="control-label"><op:translate key="DOCUMENT_METADATA_FORMAT_LABEL" /></label>
							<div class="radio">
								<label> <input type="radio" name="inline-values"
									value="pdf" ${empty document.properties['rshr:format'] or document.properties['rshr:format'] eq 'pdf' ? 'checked' : ''}> Pdf
								</label>
							</div>
							<div class="radio">
								<label> <input type="radio" name="inline-values"
									value="native" ${document.properties['rshr:format'] eq 'native' ? 'checked' : ''}> Format natif
								</label>
							</div>
						</div>
		                
		                <input type="submit" class="hidden">
		            </form>                 
	                
	                <c:if test="${not empty targets}">
	                    <dt><op:translate key="SHARED_TARGET" /></dt>
	                    <c:forEach var="target" items="${targets}">
	                        <dd>
	                            <span>${target.pubOrganization}</span>
	                            /<span>${target.pubGroup}</span>
	                            /<span>${target.pubContext}</span>
	                        </dd>
	                    </c:forEach>
	                </c:if>
	            
	        
	        
	            <!-- Remote publication spaces -->
	            <dl class="no-margin">
	                <ttc:include page="metadata-remote-sections.jsp" />
	            </dl>
	        </div>
	    </div>
    </c:if>
</div>
