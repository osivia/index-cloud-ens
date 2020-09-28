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
    <div class="container-fluid d-flex flex-column flex-grow-1">
        <div class="row flex-grow-1 flex-nowrap overflow-auto">
            <%@include file="../includes/nav.jspf" %>

            <div class="col-md d-md-flex flex-column overflow-auto py-4 p-md-5 bg-green-light background-clouds">
                <p:region regionName="top"/>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
