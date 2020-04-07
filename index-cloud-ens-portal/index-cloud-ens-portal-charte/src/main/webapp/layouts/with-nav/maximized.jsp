<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="fullheight overflow-hidden d-flex flex-column" data-drawer="true">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container-fluid d-flex flex-column flex-grow-1 overflow-hidden">
        <div class="row flex-grow-1 overflow-hidden">
            <%@include file="../includes/nav.jspf" %>

            <div class="col-md d-flex flex-column mh-100 overflow-hidden">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="row flex-grow-1 pt-4 overflow-auto mh-100">
                    <div class="col">
                        <p:region regionName="maximized"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
