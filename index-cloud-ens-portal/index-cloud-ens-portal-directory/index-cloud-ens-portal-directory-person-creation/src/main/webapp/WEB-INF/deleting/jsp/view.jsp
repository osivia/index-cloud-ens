<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:actionURL name="deleteAccount" var="deleteAccountUrl"/>

<div class="page-heade">
	<div class="h2 m-0"><op:translate key="deleteaccount.title" /></div>
</div>
<op:translate key="deleteaccount.info" />

<div class="delete-account" data-placeholder data-url="${deleteAccountUrl}" data-userDisplayName="${form.displayName}">

	<div class="form-group row required">
		<label class="col-md-6 col-form-label"><op:translate key="deleteaccount.form.displayName" /></label>
        <div class="col-md-6">
        	<input id="displayName" type="text" name="displayName" class="form-control" />
        </div>
	</div>
	
	<div class="form-group row required">
		<label class="col-md-6 col-form-label"><op:translate key="deleteaccount.form.checkbox" /></label>
        <div class="col-md-6">
        	<div class="checkbox">
	        	<input id="agreement" type="checkbox" name="agreement" />
	        	<op:translate key="deleteaccount.form.agreement" />
        	</div>
        </div>
	</div>		


	<div class="form-group row">
	    <div class="col-md-9 offset-md-3 col-lg-10 offset-lg-2">
	        <!-- Save -->
	        <button id="delete-account-button" class="btn btn-danger" disabled="disabled">
	            <span><op:translate key="deleteaccount.submit" /></span>
	        </button>
	    </div>
	</div>

</div>
