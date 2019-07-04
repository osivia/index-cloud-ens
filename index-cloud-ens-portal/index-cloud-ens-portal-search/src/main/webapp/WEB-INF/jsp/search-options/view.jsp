<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<%@ page isELIgnored="false" %>


<portlet:actionURL name="search" var="searchUrl"/>
<portlet:actionURL name="clear-location" var="clearLocationUrl"/>

<portlet:resourceURL id="loadLevels" var="loadLevelsUrl"/>


<form:form action="${searchUrl}" method="post" modelAttribute="form">
    <%--Level--%>
    <div class="form-group row">
        <form:label path="level" cssClass="col-md-3 col-form-label"><op:translate
                key="SEARCH_OPTIONS_LEVEL_LABEL"/></form:label>
        <div class="col-md-6">
            <c:set var="select2Searching"><op:translate key="SELECT2_SEARCHING"/></c:set>
            <form:select path="level" cssClass="form-control select2 select2-default" data-url="${loadLevelsUrl}"
                         data-searching="${select2Searching}">
                <c:choose>
                    <c:when test="${empty form.level}">
                        <form:option value=""><op:translate key="SEARCH_OPTIONS_LEVEL_ALL"/></form:option>
                    </c:when>

                    <c:otherwise>
                        <form:option value="${form.level}"><ttc:vocabularyLabel name="idx_level" key="${form.level}"/></form:option>
                    </c:otherwise>
                </c:choose>
            </form:select>
        </div>
    </div>

    <%--Location--%>
    <div class="form-group row">
        <form:label path="location" cssClass="col-md-3 col-form-label"><op:translate
                key="SEARCH_OPTIONS_LOCATION_LABEL"/></form:label>
        <div class="col-md-6">
            <p class="form-control-plaintext">
                <c:choose>
                    <c:when test="${empty form.location}"><op:translate
                            key="SEARCH_OPTIONS_LOCATION_ANYWHERE"/></c:when>
                    <c:otherwise><ttc:title document="${form.location}" linkable="false" icon="true"/></c:otherwise>
                </c:choose>
            </p>
        </div>
        <c:if test="${not empty form.location}">
            <div class="col-md-3">
                <a href="${clearLocationUrl}" class="btn btn-link">
                    <small><op:translate key="SEARCH_OPTIONS_LOCATION_CLEAR"/></small>
                </a>
            </div>
        </c:if>
    </div>

    <hr>

    <%--Keywords--%>
    <div class="form-group row">
        <form:label path="keywords" cssClass="col-md-3 col-form-label"><op:translate
                key="SEARCH_OPTIONS_KEYWORDS_LABEL"/></form:label>
        <div class="col-md-9">
            <c:set var="placeholder"><op:translate key="SEARCH_OPTIONS_KEYWORDS_PLACEHOLDER"/></c:set>
            <form:input path="keywords" cssClass="form-control" placeholder="${placeholder}"/>
        </div>
    </div>

    <%--Buttons--%>
    <div class="row">
        <div class="col-md-9 offset-md-3">
                <%--Submit--%>
            <button type="submit" class="btn btn-primary">
                <span><op:translate key="SEARCH_OPTIONS_SUBMIT"/></span>
            </button>

                <%--Cancel--%>
            <button type="button" class="btn btn-secondary ml-2" data-dismiss="modal">
                <span><op:translate key="CANCEL"/></span>
            </button>
        </div>
    </div>
</form:form>
