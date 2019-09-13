<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
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
    <authz:authorize access="hasAnyRole('ROLE_USER')">
        <form id="confirmationForm" name="confirmationForm" action="<%=request.getContextPath()%>/oauth/authorize"
              method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="user_oauth_approval" value="true"/>

            <fieldset>
                <legend>
                    <span>Confirmation d'autorisations</span>
                </legend>

                <p>
                    <span>J'autorise l'application <b><c:out value="${clientName}"/></b> à&nbsp;:</span>
                </p>

                <c:forEach items="${scopes}" var="scope" varStatus="status">
                    <c:set var="approved">
                        <c:if test="${scope.value}"> checked</c:if>
                    </c:set>
                    <c:set var="denied">
                        <c:if test="${!scope.value}"> checked</c:if>
                    </c:set>

                    <div class="form-group">
                        <div class="card">
                            <div class="card-body">
                                <label>
                                    <c:choose>
                                        <c:when test="${scope.key eq 'scope.drive'}"><b>Acc&eacute;der &agrave; mes
                                            fichiers</b></c:when>
                                        <c:otherwise>${scope.key}</c:otherwise>
                                    </c:choose>
                                </label>
                                <div>
                                    <div class="form-check form-check-inline">
                                        <input id="approved-${status.index}" type="radio" name="${scope.key}" value="true"
                                               class="form-check-input" ${scope.value ? 'checked' : ''}>
                                        <label for="approved-${status.index}" class="form-check-label">Approuver</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input id="denied-${status.index}" type="radio" name="${scope.key}" value="false"
                                               class="form-check-input" ${scope.value ? '' : 'checked'}>
                                        <label for="denied-${status.index}" class="form-check-label">Refuser</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <div class="text-right">
                    <button class="btn btn-primary" type="submit">Confirmer</button>
                </div>
            </fieldset>
        </form>
    </authz:authorize>
</div>

</body>

</html>
