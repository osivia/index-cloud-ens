
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

<link href="<c:url value="/components/PDFViewer/css/viewer.css" />" rel="stylesheet">


<script type="text/javascript"	src="<c:url value="/components/jquery/jquery-1.12.4.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/components/jquery/portal-compatibility.js" />"></script>

<script type="text/javascript" src="<c:url value="/components/PDFViewer/preview.js" />"></script>

</head>
<body>

<!-- Preview in iframe -->
<iframe src="<c:url value="/components/PDFViewer/web/viewer.html"/>" width="100%" height="800" webkitallowfullscreen="" allowfullscreen="" class="pdf-preview-iframe hidden" data-preview-url="${downloadURL}" onload="downloadPreview();"></iframe>
 

</body>
</html>
