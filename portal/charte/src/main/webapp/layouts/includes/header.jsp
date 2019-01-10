<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<!-- Toolbar -->
<p:region regionName="toolbar" />

<header class="hidden-xs">
    <div class="${editorial ? 'container' : 'container-fluid'}">
        <!-- Title -->
        <div class="${editorial ? 'jumbotron' : 'sr-only'}">
            <h1 class="text-center"><op:translate key="PORTAL_TITLE" /></h1>
        </div>
    
        <!-- Tabs -->
        <div class="tabs tabs-default">
            <p:region regionName="tabs" />
        </div>
    </div>
</header>
