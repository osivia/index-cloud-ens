<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column">

<header>
    <%--Toolbar--%>
    <p:region regionName="toolbar"/>

    <%--Title--%>
    <div class="text-white bg-primary p-5">
        <h1 class="text-center">
            <span><op:translate key="PORTAL_TITLE"/></span>
        </h1>
    </div>
</header>

<main class="d-flex flex-column flex-grow-1 overflow-auto">
    <div class="container">
        <div class="py-5">
            <p:region regionName="col-1"/>
        </div>
    </div>
</main>

<%@include file="../includes/footer.jspf" %>

</body>

</html>
