<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:defineObjects/>


<portlet:actionURL name="submitForm" var="submitFormUrl"/>
<portlet:resourceURL id="password-information" var="passwordInformationUrl"/>

<div class="page-heade">
	<div class="h2 m-0"><op:translate key="createaccount.title" /></div>
</div>

<div class="create-account">

	<form:form action="${submitFormUrl}" method="post" modelAttribute="form" role="form">
	
		<spring:bind path="firstname">
		    <div class="form-group row required">
		        <form:label path="firstname" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="createaccount.form.firstname" /></form:label>
		        <div class="col-md-9 col-lg-10">
		            <form:input path="firstname" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />
		            <form:errors path="firstname" cssClass="invalid-feedback" />
		        </div>
		    </div>
		</spring:bind>
		
		<spring:bind path="lastname">
		    <div class="form-group row required">
		        <form:label path="lastname" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="createaccount.form.lastname" /></form:label>
		        <div class="col-md-9 col-lg-10">
		            <form:input path="lastname" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />
		            <form:errors path="lastname" cssClass="invalid-feedback" />
		        </div>
		    </div>
		</spring:bind>		
	
		<spring:bind path="mail">
		    <div class="form-group row required">
		        <form:label path="mail" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="createaccount.form.mail" /></form:label>
		        <div class="col-md-9 col-lg-10">
		            <form:input path="mail" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />
		            <form:errors path="mail" cssClass="invalid-feedback" />
		        </div>
		    </div>
		</spring:bind>
		
		<spring:bind path="newpassword">
		    <div class="form-group row required ${status.error ? 'has-error has-feedback' : ''}">
		        <form:label path="newpassword" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="createaccount.form.newpassword" /></form:label>
		        <div class="col-md-9 col-lg-10">
		            <form:password path="newpassword" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />
		            <form:errors path="newpassword" cssClass="invalid-feedback" />
		        </div>
		    </div>
		    
	        <%--Password rules information--%>
	        <div class="row">
	            <div class="col-md-offset-3 col-md-9">
	                <div class="panel panel-default">
	                    <div class="panel-body">
	                        <p><op:translate key="createaccount.password.rules"/></p>
	                        <div data-password-information-placeholder data-url="${passwordInformationUrl}"></div>
	                    </div>
	                </div>
	            </div>
	        </div>	    
		    
		</spring:bind>
		
		<spring:bind path="confirmpassword">
		    <div class="form-group row required">
		        <form:label path="confirmpassword" cssClass="col-md-3 col-lg-2 col-form-label"><op:translate key="createaccount.form.confirmpassword" /></form:label>
		        <div class="col-md-9 col-lg-10">
		            <form:password path="confirmpassword" cssClass="form-control ${status.error ? 'is-invalid' : ''}" />
		            <form:errors path="confirmpassword" cssClass="invalid-feedback" />
		        </div>
		    </div>
		</spring:bind>		
	
		<div class="form-group row">
		    <div class="col-md-9 offset-md-3 col-lg-10 offset-lg-2">
		        <!-- Save -->
		        <button type="submit" name="save" class="btn btn-primary">
		            <span><op:translate key="createaccount.submit" /></span>
		        </button>
		    </div>
		</div>
	
	
	</form:form>
</div>