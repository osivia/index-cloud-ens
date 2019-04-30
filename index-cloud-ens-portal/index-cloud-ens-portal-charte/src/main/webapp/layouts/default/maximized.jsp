<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column">

<%@include file="../includes/header.jspf" %>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container-fluid d-flex flex-column flex-grow-1 overflow-hidden">
        <div class="row flex-grow-1 overflow-hidden">
            <op:resizable cssClass="col-sm-auto d-flex flex-column mh-100 pl-0 border-right border-light"
                          minWidth="200">
                <div class="mt-4 flex-grow-1 overflow-auto">
                    <p:region regionName="nav"/>
                </div>

                <div class="row my-4 border-top border-light">
                    <div class="col pr-0">
                        <div class="position-relative p-3">
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" style="width: 25%" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                            <div>
                                <small class="text-muted">
                                    <span>2,5 Go utilis&eacute;s sur 10 Go</span>
                                    <br>
                                    <a href="#" class="stretched-link">Obtenir plus d'espace</a>
                                </small>
                            </div>
                        </div>

                        <p:region regionName="nav-bottom"/>
                    </div>
                </div>
            </op:resizable>

            <div class="col-sm d-flex flex-column mh-100 overflow-auto">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="mb-2 d-flex flex-column flex-grow-1">
                    <p:region regionName="maximized"/>
                </div>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
