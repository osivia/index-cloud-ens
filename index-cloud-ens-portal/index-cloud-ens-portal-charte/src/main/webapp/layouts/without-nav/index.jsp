<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container-fluid d-flex flex-column flex-grow-1 overflow-hidden">
        <%@include file="../includes/breadcrumb.jspf" %>

        <div class="d-flex flex-column flex-grow-1 overflow-x-hidden overflow-y-auto">
            <p:region regionName="top"/>

            <div class="row">
                <div class="col-md d-flex flex-column">
                    <div class="flex-grow-1">
                        <p:region regionName="col-1"/>
                    </div>
                </div>

                <div class="col-md d-flex flex-column">
                    <div class="flex-grow-1">
                        <p:region regionName="col-2"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
