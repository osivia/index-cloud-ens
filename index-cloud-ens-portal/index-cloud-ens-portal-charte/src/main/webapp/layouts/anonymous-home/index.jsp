<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>

<head>
<%@include file="../includes/head.jspf"%>
</head>


<body class="fullheight overflow-hidden d-flex flex-column">

	<%@include file="../includes/header.jspf"%>



	<main class="d-flex flex-column flex-grow-1 overflow-auto bg-green-light background-clouds">
	<div class="container d-flex flex-row flex-grow-1 flex-shrink-0 justify-content-center py-4">

		<div class="my-auto">
			<img alt="Cloud PRONOTE" src="/index-cloud-ens-charte/img/logo-cloud-pronote.svg">
		</div>
		<div class="ml-3 my-auto">

			<p class="mb-1">
				<strong>Utiliser le Cloud PRONOTE c'est&nbsp;:</strong>
			</p>
			<ul class="pl-3 text-green-dark small">
				<li class="mb-1"><strong>avoir la garantie de rester propri&eacute;taire de vos documents</strong></li>
				<li class="mb-1"><strong>pouvoir cl&ocirc;turer votre compte &agrave; tout moment</strong></li>
				<li class="mb-1"><strong>&ecirc;tre assur&eacute; que vos donn&eacute;es personnelles ne seront pas
						exploit&eacute;es</strong></li>
				<li class="mb-1"><strong>pouvoir r&eacute;cup&eacute;rer tout ou partie de vos documents quand vous le
						souhaitez</strong></li>
			</ul>
		</div>

	</div>
	</main>

	<%@include file="../includes/footer.jspf"%>

</body>

</html>
