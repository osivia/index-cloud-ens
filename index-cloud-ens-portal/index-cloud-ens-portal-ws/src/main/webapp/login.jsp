<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>


<meta charset="UTF-8">
<title>Cloud Index Education</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<meta http-equiv="cache-control"	content="no-cache, no-store, must-revalidate">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">

<meta name="application-name" content="Cloud Index Education">


<meta http-equiv="default-style" content="Demo">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<script type='text/javascript'
	src='/osivia-portal-custom-web-assets/components/jquery/jquery-1.12.4.min.js'></script>
<script type='text/javascript'
	src='/osivia-portal-custom-web-assets/components/jquery-ui/jquery-ui-1.11.3.min.js'></script>
<link rel='stylesheet'
	href='/osivia-portal-custom-web-assets/components/jquery-ui/jquery-ui-1.11.3.min.css'>
<script type='text/javascript'
	src='/osivia-portal-custom-web-assets/components/jquery-mobile/jquery.mobile.custom.min.js'></script>
<link rel='stylesheet'
	href='/osivia-portal-custom-web-assets/css/bootstrap.min.css'
	title='Bootstrap'>
<link rel='stylesheet'
	href='/osivia-portal-custom-web-assets/css/osivia.min.css'>


<link rel="stylesheet" href="/demo-charte/css/demo.min.css" title="Demo" />

</head>
<body>
	<div class="container">
		<c:if test="${not empty param.authentication_error}">
			<p class="text-danger">Vos identifiants sont incorrects.</p>
		</c:if>
		<c:if test="${not empty param.authorization_error}">
			<p class="text-danger">Vous n'avez pas les droits pour accéder à cette ressource</p>
		</c:if>

		<div class="form-horizontal">
			<form action="<c:url value="/login"/>" method="post" role="form">
				<fieldset>
					<legend>
						<h2>Authentification</h2>
					</legend>
					<div class="form-group">
						<label for="username">Mail:</label> <input id="username"
							class="form-control" type='text' name='username'
							 />
					</div>
					<div class="form-group">
						<label for="password">Mot de passe:</label> <input id="password"
							class="form-control" type='password' name='password' />
					</div>
					<button class="btn btn-primary" type="submit">Connexion</button>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</fieldset>
			</form>

		</div>


	</div>


</body>
</html>
