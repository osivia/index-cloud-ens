<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<div class="portlet-filler d-flex flex-column flex-grow-1">
    <ttc:include page="toolbar.jsp"/>

    <div class="row flex-grow-1 overflow-auto">
        <div class="col-md-8 col-lg-9 d-flex flex-column mh-100">
            <div class="d-flex flex-column flex-grow-1">
                <ttc:include page="view-${dispatchJsp}.jsp"/>
            </div>
        </div>

        <div class="col-md-4 col-lg-3 d-flex flex-column mh-100">
            <!-- 
            pr-md-1 mt-1 mt-md-0 overflow-md
            On mobile, rigth column is under main content -> insert top margin and disable overflow (to avoid double sidebar)
             -->
            <div class="d-flex flex-column flex-grow-1 pr-md-1 mt-1 mt-md-0 overflow-md ">
                <%--Document attachments view--%>
                <c:if test="${attachments}">
                    <ttc:include page="attachments.jsp"/>
                </c:if>

                <%--Metadata--%>
                <c:if test="${metadata}">
                    <ttc:include page="metadata.jsp"/>
                </c:if>
            </div>
        </div>
    </div>
</div>
