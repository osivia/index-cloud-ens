<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<div id="files"></div>

<form method="POST" enctype="multipart/form-data" id="fileUploadForm">
    <input type="hidden" name="folderId" id="folderId"/>
    <input type="hidden" name="extraField"/>
    <input type="file" name="file"/><br/>
    <input type="button" value="Submit" id="btnSubmit"/>
</form>

<script>
	drive();
</script>

