<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="fullheight overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 flex-basis-0">
	<div class="container-fluid d-flex flex-column flex-grow-1 flex-basis-0">
		<div class="row flex-grow-1 flex-basis-0">
			<div class="col-lg-9 d-flex flex-column mh-100 p-5 bg-green-light">
				<p:region regionName="col-1-top" />

				<div class="row flex-grow-1 flex-basis-0 overflow-auto">
					<div class="col-lg-4">
						<p:region regionName="col-1-1" />
					</div>

					<div class="col-lg-4">
						<p:region regionName="col-1-2" />
					</div>

					<div class="col-lg-4">
						<p:region regionName="col-1-3" />
					</div>
				</div>
			</div>

			<div class="col-lg-3 d-flex flex-column mh-100 p-4 bg-orange-light shadow">
				<div class="flex-grow-1 overflow-auto">
					<p:region regionName="col-2" />
				</div>

				<div class="flex-shrink-0 text-center mt-4">
					<img src="${contextPath}/img/logo-cloud-pronote-large.png" alt="Cloud PRONOTE" height="56">
				</div>
			</div>
		</div>
	</div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
