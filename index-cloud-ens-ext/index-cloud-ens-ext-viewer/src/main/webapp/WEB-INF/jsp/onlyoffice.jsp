
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>


<meta charset="UTF-8">
<title>Cloud Index Education</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">

<meta name="application-name" content="Cloud Index Education">


<meta http-equiv="default-style" content="Viewer">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<link href="<c:url value="/components/onlyoffice/css/viewer.css" />" rel="stylesheet">


<script type="text/javascript" src="/onlyoffice/web-apps/apps/api/documents/api.js"></script>
</head>
<body class="fixed-layout">

<div class="flexbox">
	<div id="onlyoffice-placeholder" data-onlyoffice-config='{"document":{"title":"${fileName}","fileType":"${extension}","url":"${downloadURL}"},"height":"","documentType":"${fileType}","width":"","type":"","editorConfig":{"customization":{"chat":false},"lang":"fr-FR","mode":"view"}}' class="flexbox"></div>
</div>


<script type="text/javascript">
       var config = JSON.parse(document.getElementById('onlyoffice-placeholder').getAttribute('data-onlyoffice-config'));
       var docEditor = new DocsAPI.DocEditor("onlyoffice-placeholder", config);
</script>


</body>
</html>
