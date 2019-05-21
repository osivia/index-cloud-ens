<%@ page
	import="org.springframework.security.core.AuthenticationException"%>
<%@ page
	import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter"%>
<%@ page
	import="org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					<legend>
						<h2>Confirmation d'autorisations</h2>
					</legend>
			<p>
				J'autorise 	l'application <b><c:out value="${clientName}" /> </b>
				 à :
			</p>

			<form id="confirmationForm" name="confirmationForm"
				action="<%=request.getContextPath()%>/oauth/authorize" method="post">
				<input name="user_oauth_approval" value="true" type="hidden" />

					<c:forEach items="${scopes}" var="scope">
						<c:set var="approved">
							<c:if test="${scope.value}"> checked</c:if>
						</c:set>
						<c:set var="denied">
							<c:if test="${!scope.value}"> checked</c:if>
						</c:set>
						
						<c:set var="scopeLabel">
							<c:choose>
								<c:when test="${scope.key eq 'scope.drive'}"> <b>Accéder à mes fichiers </b></c:when>
							<c:otherwise>
							    ${scope.key}
							  </c:otherwise>
							</c:choose>								
						</c:set>
						<div class="card mb-3">
							<div class="card-body">						
								<div class="form-group">
									${scopeLabel} <br/> <input type="radio" name="${scope.key}"
										value="true" ${approved}>Approuver</input> <input type="radio"
										name="${scope.key}" value="false" ${denied}>Refuser</input>
								</div>
							</div>
						</div>
					</c:forEach>

				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
				<button class="btn btn-primary" type="submit">Confirmer</button>
			</form>

		</authz:authorize>

	</div>

</body>
</html>
