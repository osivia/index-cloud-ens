<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="fullheight overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
	<div class="container-fluid d-flex flex-column flex-grow-1">
		<div class="row flex-lg-grow-1 flex-lg-nowrap overflow-auto">
			<div class="col-lg-9 d-lg-flex flex-column overflow-auto p-5 bg-green-light background-clouds">
				<p:region regionName="col-1-top" />

				<div class="row flex-lg-grow-1 flex-lg-nowrap">
					<div class="col-lg-3 d-lg-flex flex-column overflow-auto">
						<p:region regionName="col-1-1" />
					</div>

					<div class="col-lg-4 d-lg-flex flex-column overflow-auto">
						<p:region regionName="col-1-2" />
					</div>

					<div class="col-lg-5 d-lg-flex flex-column overflow-auto">
						<p:region regionName="col-1-3" />
					</div>
				</div>
			</div>

			<div class="col-lg-3 d-lg-flex flex-column p-4 bg-orange-light shadow">
				<div class="row flex-lg-grow-1 flex-lg-nowrap">
					<div class="col d-lg-flex flex-column overflow-auto">
						<p:region regionName="col-2" />
					</div>
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
