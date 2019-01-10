<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="fixed-layout">
    <jsp:include page="../includes/header.jsp" />
    
    <main>
        <div class="container-fluid flexbox">
            <!-- Content navbar -->
            <jsp:include page="../includes/content-navbar.jsp" />
            
            <p:region regionName="cols-top" />
            
            <div class="row flexbox">
                <!-- Drawer -->
                <div id="drawer" class="col-sm-5 col-md-4 col-lg-3 flexbox">
                    <div class="row">
                        <p:region regionName="drawer-toolbar" />
                    </div>
                    
                    <div class="scrollbox">
                        <p:region regionName="col-1" />
                    </div>
                </div>
                
                <div class="col-sm-7 col-md-8 col-lg-9 flexbox">
                    <div class="scrollbox">
                        <p:region regionName="col-2" />
                    </div>
                </div>
            </div>
            
            <p:region regionName="cols-bottom" />
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
