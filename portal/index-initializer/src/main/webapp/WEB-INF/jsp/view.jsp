<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxcall.js"></script>

<style>
.container{
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
}


</style>


<portlet:defineObjects />
<portlet:actionURL var="checkInitialized">
	<portlet:param name="action" value="checkInit" />
</portlet:actionURL>

<div class="container" data-url="${checkInitialized}" data-service="waitUntilInitialized">
    	<div>
    		<img src="${pageContext.request.contextPath}/images/spinner.gif" alt="" > 
			<op:translate key="WAIT" />
		</div>
</div>