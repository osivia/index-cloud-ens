<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column" data-drawer="true">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container-fluid d-flex flex-column flex-grow-1 overflow-hidden">
        <%@include file="../includes/breadcrumb.jspf" %>

        <div class="d-flex flex-column flex-grow-1 overflow-x-hidden overflow-y-auto">
            <p:region regionName="top"/>

            <div class="row flex-grow-1 overflow-hidden">
                <div class="col-md-4 d-flex flex-column mh-100">
                    <div class="row flex-grow-1 overflow-hidden">
                        <%--Drawer--%>
                        <div id="drawer" class="col d-flex flex-column mh-100">
                            <p:region regionName="drawer-header" />

                            <div class="flex-grow-1 py-4 py-md-0 overflow-auto">
                                <p:region regionName="col-1"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md d-flex flex-column mh-100">
                    <div class="flex-grow-1 overflow-auto">
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
