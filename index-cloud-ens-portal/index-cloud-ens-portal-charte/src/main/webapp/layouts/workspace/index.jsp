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
                                <div class="position-relative p-3">
                                    <div class="progress">
                                        <div class="progress-bar" role="progressbar" style="width: 25%" aria-valuenow="25"
                                             aria-valuemin="0" aria-valuemax="100"></div>
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
                    </div>
                </div>
            </op:resizable>

            <div class="col-md d-flex flex-column mh-100">
                <%@include file="../includes/breadcrumb.jspf" %>

                <div class="d-flex flex-column flex-grow-1 overflow-x-hidden overflow-y-auto">
                    <p:region regionName="top"/>

                    <div class="row">
                        <div class="col-md d-flex flex-column">
                            <div class="flex-grow-1">
                                <p:region regionName="col-1"/>

                                <h2 class="h6 text-muted">Ma s&eacute;curit&eacute;</h2>

                                <%--Mes donn&eacute;es--%>
                                <div class="card mb-3">
                                    <div class="card-body">
                                        <h3 class="h5 card-title">Mes donn&eacute;es</h3>
                                        <p class="card-text">Par respect de la transparence vis-&agrave;-vis de la
                                            protection des donn&eacute;es personnelles, vous pouvez consulter l'ensemble
                                            de vos donn&eacute;es personnelles conserv&eacute;es sur nos serveurs.</p>
                                        <a href="#" class="card-link">T&eacute;l&eacute;charger mes donn&eacute;es</a>
                                    </div>
                                </div>

                                <%--Supprimer mon compte--%>
                                <div class="card border-danger mb-3">
                                    <div class="card-body text-danger">
                                        <h3 class="h5 card-title">Suppression de mon compte</h3>
                                        <p class="card-text">La suppression de votre compte effacera de mani&egrave;re d&eacute;finitive
                                            l'ensemble de vos donn&eacute;es personnelles conserv&eacute;es sur nos
                                            serveurs.</p>
                                        <a href="#" class="btn btn-danger">Supprimer mon compte</a>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <div class="col-md d-flex flex-column">
                            <div class="flex-grow-1">
                                <p:region regionName="col-2"/>

                                <h2 class="h6 text-muted">Espace communautaire</h2>

                                <%--Mes publications--%>
                                <div class="card mb-3">
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
