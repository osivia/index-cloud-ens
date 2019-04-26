<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="home" value="true" scope="request" />
<c:set var="editorial" value="true" scope="request" />


<html>

<head>
    <jsp:include page="../includes/head.jspf" />
</head>


<body class="home editorial">
    <jsp:include page="../includes/header.jspf" />
    
    <main>
        <div class="container">
            <!-- Content navbar -->
            <jsp:include page="../includes/content-navbar.jsp" />
            
            <div class="row">
                <!-- Drawer -->
                <div id="drawer">
                    <p:region regionName="drawer-toolbar" />
                    
                    <div class="col-md-4 col-lg-3">
                        <p:region regionName="col-1" />
                    </div>
                </div>
                
                <div class="col-md-8 col-lg-9">
                    <p:region regionName="maximized" />
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jspf" />
</body>

</html>
