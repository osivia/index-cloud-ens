<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="save" var="url"/>


<form:form action="${url}" method="post" modelAttribute="windowProperties" role="form">
    <%--Title--%>
    <div class="form-group">
        <form:label path="title"><op:translate key="FILTER_ADMIN_TITLE_LABEL"/></form:label>
        <form:input path="title" cssClass="form-control"/>
    </div>

    <%--Selector identifier--%>
    <div class="form-group">
        <form:label path="selectorId"><op:translate key="FILTER_ADMIN_SELECTOR_ID_LABEL"/></form:label>
        <form:input path="selectorId" cssClass="form-control"/>
    </div>

    <%--Vocabulary--%>
    <div class="form-group">
        <form:label path="vocabulary"><op:translate key="FILTER_ADMIN_VOCABULARY_LABEL"/></form:label>
        <form:input path="vocabulary" cssClass="form-control"/>
    </div>

    <%--Buttons--%>
    <div>
        <button type="submit" class="btn btn-primary"><op:translate key="SAVE"/></button>
        <button type="button" onclick="closeFancybox()" class="btn btn-secondary"><op:translate key="CANCEL"/></button>
    </div>
</form:form>
