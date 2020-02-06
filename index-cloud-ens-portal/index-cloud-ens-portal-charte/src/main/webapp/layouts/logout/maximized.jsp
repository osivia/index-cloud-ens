<!DOCTYPE html>
<%@ taglib prefix="p" uri="portal-layout" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<html>

<head>
    <%@include file="../includes/head.jspf" %>
</head>


<body class="vh-100 overflow-hidden d-flex flex-column uncluttered" data-page-header="true">

<header>
    <%--Simple toolbar--%>
    <p:region regionName="logout" />
</header>


<%@include file="../includes/footer.jspf" %>

</body>

</html>
