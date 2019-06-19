<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:actionURL name="save" var="saveUrl"/>

<form:form action="${saveUrl}" method="post" modelAttribute="form" role="form">

	<spring:bind path="mail">
	    <div class="form-group row required">
	        <form:label path="mail" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="renew.form.mail" /></form:label>
	        <div class="col-md-9 col-lg-10">
	            <form:input path="mail" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />
	            <form:errors path="mail" cssClass="invalid-feedback" />
	        </div>
	    </div>
	</spring:bind>




	<c:choose>
		<c:when test="${not form.sent}">
		
			<div class="form-group row">
			    <div class="col-md-9 offset-md-3 col-lg-10 offset-lg-2">
			        <!-- Save -->
			        <button type="submit" name="save" class="btn btn-primary">
			            <i class="glyphicons glyphicons-floppy-disk"></i>
			            <span><op:translate key="SAVE" /></span>
			        </button>
			    </div>
			</div>
		
		</c:when>
		<c:otherwise>
			
			<div class="form-group row">
			
			
			    <div class="col-md-9 offset-md-3 col-lg-10 offset-lg-2">
			    
			    	<div class="text-success"><op:translate key="renew.form.sent" /></div>
			    
			        <!-- Close -->
			        <a class="btn btn-default" onclick="window.close();">
			            <span><op:translate key="CLOSE" /></span>
			        </a>
			    </div>
			</div>			
			
		</c:otherwise>
	</c:choose>


</form:form>
