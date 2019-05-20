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
        <div class="row flex-grow-1 overflow-hidden">
            <op:resizable cssClass="col-sm-auto d-flex flex-column mh-100 border-right border-light"
                          minWidth="120">
                <div class="row flex-grow-1 overflow-hidden">
                        <%--Drawer--%>
                    <div id="drawer" class="col d-flex flex-column mh-100 pl-0">
                        <p:region regionName="drawer-header" />

                        <div class="mt-4 flex-grow-1 overflow-auto">
                            <p:region regionName="nav"/>
                        </div>

                        <div class="row my-4 border-top border-light">
                            <div class="col pr-0">
                                <p:region regionName="nav-bottom"/>
                            </div>
                        </div>
                    </div>
                </div>
            </op:resizable>

            <div class="col-md d-flex flex-column mh-100">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="d-flex flex-column flex-grow-1 overflow-x-hidden overflow-y-auto">
                    <p:region regionName="maximized"/>
                </div>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
