<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<h2 class="head-title"><op:translate key="LIST_TEMPLATE_ACCESSORIES" /></h2>

<div class="row">
    <c:forEach var="document" items="${documents}">
		<div class="col-md-3 col-sm-6">
			<div class="thumbnail accessory-col">
				<a class="no-ajax-link" href=" ${document.properties['visuelUrl']}" data-fancybox="gallery" data-caption="${document.properties['visuelFilename']}" data-type="image">
				  <img src=" ${document.properties['visuelUrl']}" alt="${document.properties['visuelFilename']}" class="accessory-img">
				</a>
				<div class="caption">
					<h3>${document.properties['title']}</h3>
					<c:if test="${not empty document.properties['description']}">
	                    <div class="accessory-description">${document.properties['description']}</div>
	                </c:if>
	               
					<c:if test="${not empty document.properties['prixht']}">
	                    <p class="lead">${document.properties['prixht']} &euro;</p>
	                </c:if>
	
	                <div class="text-center no-ajax-link">
					  	<a class="btn btn-primary no-ajax-link" href="${document.properties['orderUrl']}">
					  		<i class="glyphicons glyphicons-shopping-cart"></i>
					  		<op:translate key="LIST_TEMPLATE_ACCESSORIES_ORDER"/>
					  	</a>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
</div>
