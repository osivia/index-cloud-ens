<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="fullheight overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-hidden">
    <div class="container-fluid d-flex flex-column flex-grow-1 overflow-hidden">
        <div class="row flex-grow-1 overflow-hidden">
            <div id="drawer" class="col d-flex d-md-none flex-column mh-100 overflow-hidden">
                <div class="row flex-column">
                    <p:region regionName="drawer-header"/>
                </div>
            </div>

			<div class="portlet-filler flex-grow-1 row mh-100 px-3">

				<div class="col-lg-8 d-flex flex-column home-fixed-block pt-4 pr-3 overflow-hidden">
					<p:region regionName="col-1-top" />

					<div class="row mh-100 overflow-auto">
						<div class="col-sm-4  home-fixed-block">
							<p:region regionName="col-1-1" />
						</div>

						<div class="col-sm-4  home-fixed-block">
							<p:region regionName="col-1-2" />
						</div>

						<div class="col-sm-4  home-fixed-block">
							<p:region regionName="col-1-3" />
						</div>
					</div>
				</div>

				<div class="col-lg-4 d-flex flex-column py-4 home-fixed-block bg-mutualized-lighter">
					<p:region regionName="col-2" />
				</div>

			</div>
		</div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
