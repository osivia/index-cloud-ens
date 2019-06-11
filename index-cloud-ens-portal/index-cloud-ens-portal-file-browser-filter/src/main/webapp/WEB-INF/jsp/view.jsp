<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="select" var="selectUrl"/>
<portlet:resourceURL id="load" var="loadUrl"/>


<c:set var="namespace"><portlet:namespace/></c:set>


<div class="file-browser-filter">
    <form:form action="${selectUrl}" method="post" modelAttribute="form" role="form">
        <form:select path="selection" cssClass="form-control form-control-sm select2 select2-default"
                     data-url="${loadUrl}" data-change-submit="${namespace}-submit"
                     data-placeholder="${form.windowProperties.title}" data-dropdown-css-class="file-browser-filter">
            <c:if test="${not empty form.selection}">
                <form:option value="${form.selection}">${form.selectionLabel}</form:option>
            </c:if>
        </form:select>
        <input id="${namespace}-submit" type="submit" class="d-none">
    </form:form>
</div>
