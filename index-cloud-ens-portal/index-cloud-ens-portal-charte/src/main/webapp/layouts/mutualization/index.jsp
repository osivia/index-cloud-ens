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
            <div class="col-auto d-flex flex-column mh-100 border-right ui-resizable">
                <div class="row flex-grow-1 overflow-hidden">
                    <div id="drawer" class="col d-flex flex-column mh-100 overflow-hidden">
                        <div class="row flex-grow-1 overflow-auto">
                            <div class="col pt-2 pb-4">
                                <div class="row">
                                    <div class="col mb-3 bg-primary-lighter">
                                        <div class="py-1 text-center">
                                            <strong><op:translate key="LAYOUT_MUTUALIZATION_NAV_TITLE"/></strong>
                                        </div>
                                    </div>
                                </div>

                                <p:region regionName="nav"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div class="col-md d-flex flex-column mh-100  overflow-hidden">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="row flex-grow-1 pt-4 overflow-auto mh-100">
                    <div class="col">
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
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
