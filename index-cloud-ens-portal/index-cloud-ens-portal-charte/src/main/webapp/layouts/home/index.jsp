<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container-fluid d-flex flex-column flex-grow-1">
        <div class="row flex-grow-1">
            <div id="drawer" class="col d-flex d-md-none flex-column mh-100 overflow-hidden">
                <div class="row flex-column">
                    <p:region regionName="drawer-header"/>
                </div>
            </div>

            <div class="col-lg-8 d-flex flex-column mh-100 py-4">
                <p:region regionName="col-1"/>
            </div>

            <div class="col-lg-4 d-flex flex-column mh-100 py-4 bg-mutualized-lighter">
                <p:region regionName="col-2"/>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
