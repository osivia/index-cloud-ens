<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<%@ page isELIgnored="false" %>


<portlet:actionURL name="search" var="searchUrl"/>


<div class="ml-3">
    <form:form action="${searchUrl}" method="post" modelAttribute="form">
        <div class="form-group mb-0">
            <form:label path="value" cssClass="sr-only"><op:translate key="SEARCH_INPUT_VALUE_LABEL"/></form:label>
            <div class="input-group">
                <c:set var="placeholder"><op:translate key="SEARCH_INPUT_VALUE_PLACEHOLDER"/></c:set>
                <form:input path="value" type="search" placeholder="${placeholder}"
                            cssClass="form-control"></form:input>
                <input type="submit" class="d-none"/>
                <div class="input-group-append">
                    <c:set var="title"><op:translate key="SEARCH_OPTIONS"/></c:set>
                    <button type="button" class="btn btn-outline-secondary" data-target="#osivia-modal"
                            data-load-url="${optionsUrl}" data-size="large" data-title="${title}">
                        <i class="glyphicons glyphicons-basic-set-down"></i>
                        <span class="sr-only">${title}</span>
                    </button>
                </div>
            </div>
        </div>
    </form:form>
</div>
