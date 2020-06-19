<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal"%>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice"%>

<%@ page isELIgnored="false"%>


<div class="list-my-publications home-fixed-block">
	<c:choose>
		<c:when test="${empty documents}">
			<span class="text-muted"><op:translate key="DOCUMENT_NO_MUTUALIZATION" /></span>
		</c:when>

		<c:otherwise>


			<c:forEach var="document" items="${documents}">
				<div class="card mb-2 home-fixed-block-item">
					<div class="card-body">
						<h3 class="card-title h5 mb-1 text-truncate">
							<%--Icon--%>
							<span><ttc:icon document="${document}" /></span>

							<%--Title--%>
							<c:set var="url">
								<ttc:documentLink document="${document}" />
							</c:set>
							<a href="${url}" class="stretched-link no-ajax-link"> <span><ttc:title document="${document}"
										linkable="false" /></span>
							</a>
						</h3>


						<ul class="card-text list-inline">
							<%--Views--%>
							<li class="list-inline-item"><i class="glyphicons glyphicons-basic-eye"></i> <strong>${document.properties['mtz:views']}</strong>
								<c:choose>
									<c:when test="${empty document.properties['mtz:views'] or document.properties['mtz:views'] le 1}">
										<span><op:translate key="DOCUMENT_MUTUALIZATION_VIEW" /></span>
									</c:when>
									<c:otherwise>
										<span><op:translate key="DOCUMENT_MUTUALIZATION_VIEWS" /></span>
									</c:otherwise>
								</c:choose></li>

							<%--Downloads--%>
							<li class="list-inline-item"><i class="glyphicons glyphicons-basic-save"></i> <strong>${document.properties['mtz:downloads']}</strong>
								<c:choose>
									<c:when test="${empty document.properties['mtz:downloads'] or document.properties['mtz:downloads'] le 1}">
										<span><op:translate key="DOCUMENT_MUTUALIZATION_DOWNLOAD" /></span>
									</c:when>
									<c:otherwise>
										<span><op:translate key="DOCUMENT_MUTUALIZATION_DOWNLOADS" /></span>
									</c:otherwise>
								</c:choose></li>
						</ul>
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</div>
