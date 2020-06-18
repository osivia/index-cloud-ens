<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="my-messages home-fixed-block">
	<c:choose>
		<c:when test="${empty unreadMessages.items}">
			<span class="text-muted"><op:translate key="DISCUSSION_NO_UNREAD_MESSAGE"/></span>
		</c:when>

		<c:otherwise>
			<c:forEach var="message" items="${unreadMessages.items}">
				<div class="card mb-2 home-fixed-block-item">
					<div class="card-body">

						<h3 class="card-title h5 mb-1 text-truncate">

							<a href="${message.url}" class="stretched-link no-ajax-link"> <span>${message.task.properties['pubTitle']}</span>
							</a>

						</h3>

						<p class="text-truncate card-text">${message.task.properties['message']}</p>
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</div>
