<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:set var="brand"><op:translate key="BRAND" /></c:set>


<html>

<head>
    <title><op:translate key="ERROR" /> - ${brand}</title>
    
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Socle -->
    <link rel="stylesheet" href="/osivia-portal-custom-web-assets/css/osivia.min.css">
    
    <!-- Glyphicons -->
    <link rel="stylesheet" href="/osivia-portal-custom-web-assets/css/glyphicons.min.css">
    
    <!-- Style demo -->
    <meta http-equiv="default-style" content="Demo">
    <link rel="stylesheet" href="${contextPath}/css/demo.min.css" title="Demo" />
    <link rel="icon" href="${contextPath}/img/osivia.ico" />
</head>


<body class="editorial">
    <!-- Toolbar -->
    <div class="toolbar">
        <div class="navbar navbar-default navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <div class="visible-xs">
                        <button type="button" data-toggle="drawer" class="btn btn-link navbar-btn pull-left">
                            <span>
                                <i class="halflings halflings-menu-hamburger"></i>
                                <i class="halflings halflings-arrow-right"></i>
                            </span>
                        </button>
                    
                        <!-- Title -->
                        <div class="clearfix">
                            <p class="navbar-text text-overflow"><op:translate key="ERROR" /></p>
                        </div>
                    </div>
                
                    <!-- Brand -->
                    <a href="/" class="navbar-brand hidden-xs">
                        <img alt="${brand}" src="${contextPath}/img/favicon.png">
                    </a>
                </div>
            </div>
        </div>
    </div>


    <!-- Header -->
    <header class="hidden-xs">
        <div class="container">
            <!-- Title -->
            <div class="jumbotron">
                <h1 class="text-center"><op:translate key="PORTAL_TITLE" /></h1>
            </div>
        
            <!-- Tabs -->
            <div class="tabs tabs-default">
                <nav class="tabs" role="navigation">
                    <!-- Title -->
                    <h2 class="sr-only"><op:translate key="TABS_TITLE" /></h2>
    
                    <div class="primary-tabs">
                        <div class="pull-left">
                            <ul class="home">
                                <li role="presentation">
                                    <c:set var="title"><op:translate key="HOME" /></c:set>
                                    <a href="/" title="${title}" data-toggle="tooltip" data-placement="bottom">
                                        <i class="halflings halflings-home"></i>
                                        <span class="sr-only">${title}</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    
                        <div class="fixed-tabs-container">
                            <ul>
                                <li class="active">
                                    <a href="#">
                                        <span><op:translate key="ERROR" /></span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
    </header>


    <!-- Page content -->
    <main>
        <div class="container">
            <div class="content-navbar">
                <!-- Breadcrumb -->
                <div class="content-navbar-breadcrumb">
                    <nav>
                        <h2 class="sr-only"><op:translate key="BREADCRUMB_TITLE" /></h2>
                        <ol class="breadcrumb hidden-xs">
                            <li class="active">
                                <span><op:translate key="ERROR" /></span>
                            </li>
                        </ol>
                    </nav>
                </div>
            </div>
    
            <div class="row">
                <div class="col-lg-offset-3 col-lg-6">
                    <div class="panel panel-danger">
                        <div class="panel-body">
                            <div class="page-header">
                                <h1 class="text-center text-danger">
                                    <span><op:translate key="ERROR" /></span>
                                    
                                    <c:choose>
                                        <c:when test="${(param['httpCode'] eq 401) || (param['httpCode'] eq 403) || (param['httpCode'] eq 404) || (param['httpCode'] eq 500)}">
                                            <span>${param['httpCode']}</span>
                                            <small><op:translate key="ERROR_${param['httpCode']}" /></small>
                                        </c:when>
                                        
                                        <c:otherwise>
                                            <small><op:translate key="ERROR_GENERIC_MESSAGE" /></small>
                                        </c:otherwise>
                                    </c:choose>
                                </h1>
                            </div>
                            
                            <c:if test="${(param['httpCode'] eq 401) || (param['httpCode'] eq 403) || (param['httpCode'] eq 404) || (param['httpCode'] eq 500)}">
                                <p class="lead text-center">
                                    <span><op:translate key="ERROR_${param['httpCode']}_MESSAGE" /></span>
                                </p>
                            </c:if>
                            
                            <div class="text-center">
                                <a href="/" class="btn btn-link">
                                    <span><op:translate key="BACK_TO_HOME" /></span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>    
            </div>
        </div>
    </main>
</body>

</html>
