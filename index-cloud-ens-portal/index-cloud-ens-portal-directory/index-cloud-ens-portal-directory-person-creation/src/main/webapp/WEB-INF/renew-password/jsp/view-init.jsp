<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:actionURL name="sendMail" var="sendMailUrl" copyCurrentRenderParameters="true"/>

<div class="page-heade">
	<div class="h2 m-0"><op:translate key="renew.init.title" /></div>
</div>


<form:form action="${sendMailUrl}" method="post" modelAttribute="form" enctype="multipart/form-data" role="form">

	<spring:bind path="mail">
	    <div class="form-group row required ${status.error ? 'has-error has-feedback' : ''}">
	        <form:label path="mail" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="renew.init.mail" /></form:label>
	        <div class="col-md-9 col-lg-10">
	        	
	            <form:input path="mail" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />   
	            <form:errors path="mail" cssClass="invalid-feedback" />
	        </div>
	    </div>
	</spring:bind>

	<div class="form-group row">
	    <div class="col-md-9 offset-md-3 col-lg-10 offset-lg-2">
	        <!-- Save -->
	        <button type="submit" name="save" class="btn btn-primary">
	            <span><op:translate key="renew.init.submit" /></span>
	        </button>
	    </div>
	</div>


</form:form>
