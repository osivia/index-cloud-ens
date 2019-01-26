<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"
	prefix="op"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<div class="row">
	<div class="col-lg-5">
		<h3>Drive.content</h3>
		<div class="row">
			<div class="col-lg-6">
				<div id="files"></div>
			</div>
			<div class="col-lg-6">
				<div id="detail">

						<form method="POST" enctype="multipart/form-data" id="publishForm">
						 	<div class="form-group">
								<label for="pubShare" ">Share ID</label>
								<input name="pubShare"  class="form-control" id="pubShare" readonly />
							</div>
							 <div class="form-group">					
								<label for="level">Niveau</label>
								<input name="level"  class="form-control" id="pubLevel" />
							</div>
							<input type="hidden" name="contentId" id="contentId" />							
							<input type="button" class="btn btn-primary" value="Publier" id="btnPubSubmit" />
						</form>

				</div>
			</div>
		</div>
	</div>
	<div class="col-lg-1">
	</div>
	<div class="col-lg-2">
		<h3>Drive.upload</h3>
		<div class="row">
			<form method="POST" enctype="multipart/form-data"
				id="fileUploadMetadataForm">
				<div class="form-group">					
					<label for="level">Niveau</label>
					<input name="level"  class="form-control" id="uploadMDLevel" />
				</div>				
				<input type="hidden" name="folderId" id="folderId" />
			</form>

			<form method="POST" enctype="multipart/form-data" id="fileUploadForm">

				 	<input type="file" name="file" class="btn" />

				 <input type="button" class="btn btn-primary" value="Submit" id="btnUploadSubmit" />
			</form>
		</div>
	</div>
</div>
<script>
	drive();
</script>

