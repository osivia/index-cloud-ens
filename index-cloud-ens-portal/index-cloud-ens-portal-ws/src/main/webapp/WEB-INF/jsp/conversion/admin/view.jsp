<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" copyCurrentRenderParameters="true" />


<div class="edit-portal-group">

	<form:form action="${saveUrl}" method="post" modelAttribute="conversionForm" cssClass="form-horizontal"
		enctype="multipart/form-data" role="form">

		<div class="row">
			<div class="col-lg-6">
				<fieldset>
					<legend>
						<op:translate key="CONVERSION_ADMIN_FILE_LEGEND" />
					</legend>
					<!-- file conversion -->
					<%@ include file="view/file.jspf"%>


				</fieldset>


				<c:choose>
					<c:when test="${not empty conversionForm.fileDownloadUrl}">
						<p class="mt-3"></p>

						<fieldset>
							<legend>
								<op:translate key="CONVERSION_ADMIN_PATCH" />
							</legend>

							<!-- patch- -->
							<%@ include file="view/patch.jspf"%>

						</fieldset>
					</c:when>
				</c:choose>
			</div>

			<div class="col-lg-6">
				<fieldset>
					<legend>
						<op:translate key="CONVERSION_ADMIN_LOGS_LEGEND" />
					</legend>

					<!-- logs- -->
					<%@ include file="view/logs.jspf"%>
				</fieldset>
			</div>
		</div>

	</form:form>
</div>
