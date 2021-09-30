<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="fullheight overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto bg-green-light background-clouds">
    <div class="container-fluid d-flex flex-column flex-grow-1 py-4">
        <%--Breadcrumb--%>
        <p:region regionName="breadcrumb"/>

        <p:region regionName="maximized"/>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
