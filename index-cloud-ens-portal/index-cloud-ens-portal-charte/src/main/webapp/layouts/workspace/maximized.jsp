<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="navigationPanelClosed" value="${requestScope['osivia.panels.navigation-panel.closed']}" />


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="fixed-layout">
    <jsp:include page="../includes/header.jsp" />
    
    <main>
        <div class="container-fluid flexbox">
            <div class="row flexbox">
                <op:resizable enabled="${not navigationPanelClosed}" linkedToTasks="true" cssClass="col-auto flexbox" minWidth="200">
                    <div class="row flexbox">
                        <!-- Drawer -->
                        <div id="drawer" class="col-offset-auto col-auto flexbox">
                            <div class="row flexbox">
                                <p:region regionName="drawer-toolbar" />

                                <div class="col-auto flexbox">                            
                                    <p:region regionName="col-1" />
                                </div>
        
                                <div ${navigationPanelClosed ? 'class="hidden"' : 'class="col-offset-auto col-auto flexbox"'}>
                                    <p:region regionName="navigation-panel" />
                                </div>
                            </div>
                        </div>
                    </div>
                </op:resizable>
                
                <div class="col-offset-auto col-auto flexbox">
                    <div class="scrollbox">
                    	<!-- Content navbar -->
            			<jsp:include page="../includes/content-navbar.jsp" />
                    
                        <p:region regionName="maximized" />
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
