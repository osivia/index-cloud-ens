<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container-fluid d-flex flex-column flex-grow-1 overflow-hidden">
        <div class="row flex-grow-1 overflow-hidden">
            <div class="col-sm-auto d-flex flex-column mh-100 pl-0 border-right border-light">
                <div class="my-2 flex-grow-1 overflow-auto">
                    <p:region regionName="nav"/>
                </div>
            </div>

            <div class="col-sm d-flex flex-column mh-100 overflow-auto">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="my-2 d-flex flex-column flex-grow-1">
                    <div class="mb-2">
                        <p:region regionName="top"/>
                    </div>

                    <div class="row flex-grow-1">
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
