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


                <c:if test="${not empty pageContext.request.remoteUser}">
                    <h2 class="h6 text-muted">Espace communautaire</h2>

                    <%--Mes publications--%>
                    <div class="card mb-3 bg-light">
                        <div class="card-body">
                            <h3 class="h5 card-title">Mes publications</h3>

                            <ul class="list-unstyled card-text">
                                <li class="mb-2">
                                    <a href="#">
                                        <span>Plan de travail : Sym&eacute;trie Cycle 3 </span>
                                        <small class="text-muted">23 juin 2019</small>
                                    </a>
                                </li>

                                <li>
                                    <a href="#">
                                        <span>C4-4e-Th.3_Societe-culture-et-politique-dans-la-France-XIXe.zip</span>
                                        <small class="text-muted">8 juillet 2019</small>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>


                    <%--Nouveaut&eacute;s--%>
                    <div class="card mb-3">
                        <div class="card-body">
                            <h3 class="h5 card-title">Nouveaut&eacute;s</h3>
                            <ul class="list-unstyled card-text">
                                <li class="mb-2">
                                    <a href="#">
                                        <span>S&eacute;minaire "Climat" - Tr&eacute;b&eacute;dan - Du 3 au 7 juin 2019</span>
                                        <small class="text-muted">3 juin 2019</small>
                                    </a>
                                </li>

                                <li>
                                    <a href="#">
                                        <span>Plan de Travail Probabilit&eacute;s</span>
                                        <small class="text-muted">5 juillet 2019</small>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
