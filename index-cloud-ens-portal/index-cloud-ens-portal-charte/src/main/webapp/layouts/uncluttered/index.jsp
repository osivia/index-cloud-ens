<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>


<html>

<head>
    <jsp:include page="../includes/head.jspf" />
</head>


<body class="fixed-layout">
     <jsp:include page="../includes/simple-logo.jsp" />
    
    <main>
        <div class="container-fluid flexbox">

            <div class="scrollbox">
            	<p:region regionName="top" />
                <p:region regionName="col-1" />
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jspf" />
</body>

</html>
