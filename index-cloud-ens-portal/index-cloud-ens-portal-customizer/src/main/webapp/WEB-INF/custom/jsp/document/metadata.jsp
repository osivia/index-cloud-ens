<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>


<portlet:actionURL name="cancel-inline-edition" var="cancelUrl"/>

<c:set var="namespace"><portlet:namespace/></c:set>


<div class="metadata">
    <div class="card mb-3">
 
        <div class="card-body">
			<h3 class="h5 card-title text-overflow">
	            <span><op:translate key="DOCUMENT_METADATA_CLASSIFYING"/></span>
	        </h3>           
            <!-- Levels -->
            <c:set var="levels" value="${document.properties['idxcl:levels']}"/>
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_LEVEL_PLACEHOLDER"/></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="idxcl:levels"/>
                <portlet:param name="cancel-url" value="${cancelUrl}"/>
            </portlet:actionURL>
            <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                <portlet:param name="vocabulary" value="idx_level"/>
                <portlet:param name="optgroupDisabled" value="true"/>
            </portlet:resourceURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition">
                    <label for="${namespace}-levels"><op:translate key="DOCUMENT_METADATA_LEVEL_LABEL"/></label>
                    <select id="${namespace}-levels" name="inline-values" multiple="multiple"
                            class="form-control select2 select2-inline-edition" data-url="${select2Url}"
                            data-placeholder="${placeholder}">
                        <c:forEach var="level" items="${levels}">
                            <option value="${level}" selected="selected"><ttc:vocabularyLabel name="idx_level"
                                                                                              key="${level}"/></option>
                        </c:forEach>
                    </select>
                </div>

                <input type="submit" class="d-none">
            </form>


            <!-- Subjects -->
            <c:set var="subjects" value="${document.properties['idxcl:subjects']}"/>
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_SUBJECT_PLACEHOLDER"/></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="idxcl:subjects"/>
                <portlet:param name="cancel-url" value="${cancelUrl}"/>
            </portlet:actionURL>
            <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                <portlet:param name="vocabulary" value="idx_subject"/>
                <portlet:param name="optgroupDisabled" value="true"/>
            </portlet:resourceURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition">
                    <label for="${namespace}-subjects"><op:translate key="DOCUMENT_METADATA_SUBJECT_LABEL"/></label>
                    <select id="${namespace}-subjects" name="inline-values" multiple="multiple"
                            class="form-control select2 select2-inline-edition" data-url="${select2Url}"
                            data-placeholder="${placeholder}">
                        <c:forEach var="subject" items="${subjects}">
                            <option value="${subject}" selected="selected"><ttc:vocabularyLabel name="idx_subject"
                                                                                                key="${subject}"/></option>
                        </c:forEach>
                    </select>
                </div>

                <input type="submit" class="d-none">
            </form>


            <!-- Description -->
            <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_DESCRIPTION_PLACEHOLDER"/></c:set>
            <portlet:actionURL name="inline-edition" var="submitUrl">
                <portlet:param name="property" value="dc:description"/>
                <portlet:param name="cancel-url" value="${cancelUrl}"/>
            </portlet:actionURL>
            <form action="${submitUrl}" method="post">
                <div class="form-group inline-edition mb-0">
                    <label for="${namespace}-description"><op:translate
                            key="DOCUMENT_METADATA_DESCRIPTION_LABEL"/></label>
                    <textarea id="${namespace}-description" name="inline-values" rows="4" placeholder="${placeholder}"
                              class="form-control">${document.properties['dc:description']}</textarea>
                </div>

                <input type="submit" class="d-none">
            </form>

        </div>
    </div>


   
    
    <c:set var="share" value="${document.properties['rshr:linkId']}"/>
    <c:set var="enabled" value="${document.properties['rshr:enabledLink']}"/>    
    <c:set var="targets" value="${document.properties['rshr:targets']}"/>
    

      
        <div class="card mb-3">
            <div class="card-body">
				<h3 class="h5 card-title text-overflow">
		            <span><op:translate key="DOCUMENT_METADATA_CONSULT"/></span>
		        </h3>
		        
		        
                <c:if test="${not empty targets}">
                	<p >
	                	<span>
		                    <span class="badge badge-warning">${fn:length(targets)}</span>
							<a href="#" class="no-ajax-link "data-toggle="modal" data-target="#${namespace}-targets">
								<c:choose>
								  <c:when test="${fn:length(targets) eq 1}">
								   		<op:translate key="SHARED_TARGET_REFERENCE"/>
								  </c:when>
								  <c:otherwise>
							   			<op:translate key="SHARED_TARGET_REFERENCES"/>
								  </c:otherwise>
								</c:choose>						
							</a>
						</span>
					</p>
					
					
                    
			  	   <!-- Target modal -->
				    <div id="${namespace}-targets" class="modal fade" tabindex="-1" role="dialog">
				        <div class="modal-dialog" role="document">
				            <div class="modal-content">
				                <div class="modal-header">
				                    <h5 class="modal-title">
				                        <span><op:translate key="SHARED_TARGET_TITLE"/></span>
				                    </h5>
				
				                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
				                        <span aria-hidden="true">&times;</span>
				                    </button>
				                </div>

								<div class="modal-body">
									<table class="table">
										<thead class="thead-light">
											<tr>
												<th scope="col"><op:translate key="SHARED_TARGET_REFERENCES_DATE"/></th>
												<th scope="col"><op:translate key="SHARED_TARGET_REFERENCES_GROUP"/></th>
												<th scope="col"><op:translate key="SHARED_TARGET_REFERENCES_CONTEXT"/></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="target" items="${targets}">
												<c:set var="pubDate" value="${target.pubDate}" />
												<c:set var="pubGroup" value="${target.pubGroup}" />
												<c:set var="pubContext" value="${target.pubContext}" />

												<tr>
													<td><c:if test="${not empty pubDate}">
															<fmt:formatDate value="${pubDate}" type="date" dateStyle="long" />
														</c:if></td>
													<td><c:if test="${not empty pubGroup}">
								                            	${target.pubGroup}
								                            </c:if></td>
													<td><c:if test="${not empty pubContext}">
								                            	${target.pubContext}
								                            </c:if></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>

								</div>
							</div>
				        </div>
				    </div>                     
                </c:if>		        
		        
		        
		        
		        <c:if test="${enabled}">	
	    			
    			    <div>
	    			    <span>
                			<i class="glyphicons glyphicons-link"></i>
            			</span>		        
		                <span>
		                   <a href="/s/${share}"><op:translate key="SHARED_LINK"/></a>
		                 </span>
	                 </div>
	     			
	     			
	

	      			
	                <!-- Format -->
	                
	                 <c:if test="${document.pdfConvertible}">
	 	                <portlet:actionURL name="inline-edition" var="submitUrl">
		                    <portlet:param name="property" value="rshr:format"/>
		                    <portlet:param name="cancel-url" value="${cancelUrl}"/>
		                </portlet:actionURL>
		                <form action="${submitUrl}" method="post">
		                	<c:if test="${not empty targets}">
		                		<input type="hidden" name="warn-message" value="<op:translate key="DOCUMENT_CHANGE_FORMAT_WARN_MESSAGE"/>">
		                	</c:if>
		                
		                    <div class="form-group inline-edition ml-4">
		                        <label class="control-label"><op:translate key="DOCUMENT_METADATA_FORMAT_LABEL"/></label>
		                        <div class="radio">
		                            <label> <input type="radio" name="inline-values"
		                                           value="pdf" ${empty document.properties['rshr:format'] or document.properties['rshr:format'] eq 'pdf' ? 'checked' : ''}>
		                                <op:translate key="SHARED_FORMAT_PDF"/>
		                            </label>
		                        </div>
		                        <div class="radio">
		                            <label> <input type="radio" name="inline-values"
		                                           value="native" ${document.properties['rshr:format'] eq 'native' ? 'checked' : ''}>
		                                <op:translate key="SHARED_FORMAT_NATIVE"/>
		                            </label>
		                        </div>
		                    </div>
		
		                    <input type="submit" class="d-none">
		                </form>
	                </c:if>
	

	                
   					<div>
	 		            <portlet:actionURL name="link-activation" var="deactivationUrl">
			                <portlet:param name="activate" value="false"/>
			            </portlet:actionURL>
	    			    <span>
                			<i class="glyphicons glyphicons-remove"></i>
            			</span>		        
		                <span>
		                   <a href="${deactivationUrl}"><op:translate key="SHARED_LINK_DEACTIVATE"/>
		                </span>
        
	      			</div>
	            </c:if>
	      			
      			<c:if test="${not enabled}">	    			
		            <portlet:actionURL name="link-activation" var="activationUrl">
		                <portlet:param name="activate" value="true"/>
		            </portlet:actionURL>
		            
		            <div>
		    			    <span>
	                			<i class="glyphicons glyphicons-link"></i>
	            			</span>		        
			                <span>
			                   <a href="${activationUrl}"><op:translate key="SHARED_LINK_ACTIVATE"/></a>
			                 </span>
		             </div>
	            
      			</c:if>     			
	      				            

                <!-- Remote publication spaces -->
                <dl class="no-margin">
                    <ttc:include page="metadata-remote-sections.jsp"/>
                </dl>
            </div>
        </div>
        
     

</div>
