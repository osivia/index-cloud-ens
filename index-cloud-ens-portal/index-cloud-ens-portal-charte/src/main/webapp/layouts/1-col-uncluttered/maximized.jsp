<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="fixed-layout">
     <jsp:include page="../includes/simple-logo.jsp" />
    
    <main>
        <div class="container-fluid flexbox">

            <div class="scrollbox">
            	<p:region regionName="top" />
                <p:region regionName="maximized" />
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
