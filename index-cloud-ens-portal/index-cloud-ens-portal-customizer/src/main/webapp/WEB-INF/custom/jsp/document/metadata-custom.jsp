<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page isELIgnored="false"%>

<c:set var="level" value="${document.properties['idxcl:level']}" />


                <!-- Subjects -->
                <c:if test="${not empty level}">
                    <dt><op:translate key="DOCUMENT_METADATA_LEVEL" /></dt>
                    <dd>
                        <p>
                        	<ttc:vocabularyLabel name="idx_level" key="${level}"/>
                        </p>
                    </dd>
                </c:if>
                
                