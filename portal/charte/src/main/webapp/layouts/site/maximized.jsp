<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="editorial" value="true" scope="request" />


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="editorial">
    <jsp:include page="../includes/header.jsp" />
    
    <main>
        <div class="container">
            <!-- Content navbar -->
            <jsp:include page="../includes/content-navbar.jsp" />
            
            <div class="row">
                <!-- Drawer -->
                <div id="drawer">
                    <p:region regionName="drawer-toolbar" />
                    
                    <div class="col-sm-4 col-lg-3">
                        <p:region regionName="col-1" />
                    </div>
                </div>
                
                <div class="col-sm-8 col-lg-9">
                    <p:region regionName="maximized" />
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
