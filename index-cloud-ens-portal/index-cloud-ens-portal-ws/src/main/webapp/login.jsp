<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="application-name" content="Cloud Index Education">
    <meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="0">

    <meta name="application-name" content="Cloud Index Education">

    <script type='text/javascript'
            src='/osivia-portal-custom-web-assets/components/jquery/jquery-1.12.4.min.js'></script>
    <script type='text/javascript' src='/osivia-portal-custom-web-assets/js/jquery-integration.min.js'></script>
    <script type='text/javascript'
            src='/osivia-portal-custom-web-assets/components/jquery-ui/jquery-ui-1.11.3.min.js'></script>
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/components/jquery-ui/jquery-ui-1.11.3.min.css'>
    <script type='text/javascript'
            src='/osivia-portal-custom-web-assets/components/jquery-mobile/jquery.mobile.custom.min.js'></script>
    <link rel="stylesheet" href="/index-cloud-ens-charte/css/cloud-ens.css"/>
    <script src='/osivia-portal-custom-web-assets/components/bootstrap/js/bootstrap.bundle.min.js'></script>
</head>

<body>

<div class="container">
    <c:if test="${not empty param.authentication_error}">
        <p class="text-danger">Vos identifiants sont incorrects.</p>
    </c:if>
    <c:if test="${not empty param.authorization_error}">
        <p class="text-danger">Vous n'avez pas les droits pour acc&eacute;der &agrave; cette ressource</p>
    </c:if>

    <form action="<c:url value="/login"/>" method="post" role="form">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <fieldset>
            <legend>
                <span>Authentification</span>
            </legend>

            <div class="form-group">
                <label for="username">Adresse mail:</label>
                <input id="username" type="text" name="username" class="form-control"/>
            </div>

            <div class="form-group">
                <label for="password">Mot de passe:</label>
                <input id="password" type="password" name="password" class="form-control"/>
            </div>

            <div class="text-right">
                <button type="submit" class="btn btn-primary">Connexion</button>
            </div>
        </fieldset>
    </form>
</div>

</body>

</html>
