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
                          minWidth="200">
                <div class="row flex-grow-1 overflow-hidden">
                    <%--Drawer--%>
                    <div id="drawer" class="col d-flex flex-column mh-100">
                        <p:region regionName="drawer-header" />

                        <div class="row flex-grow-1 overflow-hidden">
                            <div class="col pl-0 d-flex flex-column mh-100">
                                <div class="py-4 flex-grow-1 overflow-auto">
                                    <p:region regionName="nav"/>
                                </div>
                            </div>
                        </div>

                        <div class="border-top border-light">
                            <p:region regionName="nav-bottom"/>
                        </div>
                    </div>
                </div>
            </op:resizable>

            <div class="col-md d-flex flex-column mh-100">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="d-flex flex-column flex-grow-1 overflow-x-hidden overflow-y-auto">
                    <div class="row">
                        <div class="col-auto">
                            <p class="text-muted">
                                <small><op:translate key="FILTERS_REGION_LABEL"/></small>
                            </p>
                        </div>

                        <div class="col-auto">
                            <p:region regionName="filter-1"/>
                        </div>

                        <div class="col-auto">
                            <p:region regionName="filter-2"/>
                        </div>

                        <div class="col-auto">
                            <p:region regionName="filter-3"/>
                        </div>
                    </div>

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
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
