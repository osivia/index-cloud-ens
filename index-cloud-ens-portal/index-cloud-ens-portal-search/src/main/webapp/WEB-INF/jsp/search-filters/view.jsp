<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<%@ page isELIgnored="false" %>


<portlet:actionURL name="submit" var="submitUrl"/>

<portlet:resourceURL id="load-vocabulary" var="loadLevelsUrl">
    <portlet:param name="vocabulary" value="idx_level"/>
</portlet:resourceURL>
<portlet:resourceURL id="load-vocabulary" var="loadSubjectsUrl">
    <portlet:param name="vocabulary" value="idx_subject"/>
</portlet:resourceURL>
<portlet:resourceURL id="load-vocabulary" var="loadDocumentTypesUrl">
    <portlet:param name="vocabulary" value="idx_document_type"/>
</portlet:resourceURL>
<portlet:resourceURL id="save-search-popover" var="saveSearchPopoverUrl"/>


<c:set var="namespace"><portlet:namespace/></c:set>

<c:set var="select2Searching"><op:translate key="SELECT2_SEARCHING"/></c:set>


<div class="search-filters-container">
    <form:form action="${submitUrl}" method="post" modelAttribute="form">
        <%--Level--%>
        <div class="form-group row">
            <form:label path="level" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_LEVEL_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="level" cssClass="form-control select2 select2-default" data-url="${loadLevelsUrl}"
                             data-searching="${select2Searching}">
                    <c:choose>
                        <c:when test="${empty form.level}">
                            <form:option value=""><op:translate key="SEARCH_FILTERS_LEVEL_ALL"/></form:option>
                        </c:when>

                        <c:otherwise>
                            <form:option value="${form.level}"><ttc:vocabularyLabel name="idx_level"
                                                                                    key="${form.level}"/></form:option>
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </div>
        </div>

        <%--Subject--%>
        <div class="form-group row">
            <form:label path="subject" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_SUBJECT_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="subject" cssClass="form-control select2 select2-default"
                             data-url="${loadSubjectsUrl}"
                             data-searching="${select2Searching}">
                    <c:choose>
                        <c:when test="${empty form.subject}">
                            <form:option value=""><op:translate key="SEARCH_FILTERS_SUBJECT_ALL"/></form:option>
                        </c:when>

                        <c:otherwise>
                            <form:option value="${form.subject}"><ttc:vocabularyLabel name="idx_subject"
                                                                                      key="${form.subject}"/></form:option>
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </div>
        </div>

        <%--Document type--%>
        <div class="form-group row">
            <form:label path="documentType" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_DOCUMENT_TYPE_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="documentType" cssClass="form-control select2 select2-default"
                             data-url="${loadDocumentTypesUrl}" data-searching="${select2Searching}">
                    <c:choose>
                        <c:when test="${empty form.documentType}">
                            <form:option value=""><op:translate key="SEARCH_FILTERS_DOCUMENT_TYPE_ALL"/></form:option>
                        </c:when>
                        <c:otherwise>
                            <form:option value="${form.documentType}"><ttc:vocabularyLabel name="idx_document_type"
                                                                                           key="${form.documentType}"/></form:option>
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </div>
        </div>

        <%--Location--%>
        <div class="form-group row">
            <form:label path="location" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_LOCATION_LABEL"/></form:label>
            <div class="col-md-6">
                <c:choose>
                    <c:when test="${form.modal}">
                        <p class="form-control-plaintext">
                            <c:choose>
                                <c:when test="${empty form.location}"><op:translate
                                        key="SEARCH_FILTERS_LOCATION_ANYWHERE"/></c:when>
                                <c:otherwise><ttc:title document="${form.location}" linkable="false"
                                                        icon="true"/></c:otherwise>
                            </c:choose>
                        </p>
                    </c:when>

                    <c:otherwise>
                        <a href="javascript:" class="btn btn-outline-primary" data-target="#osivia-modal"
                           data-load-url="${locationUrl}" data-size="small"><ttc:title document="${form.location}"
                                                                                       linkable="false" icon="true"/>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
            <form:hidden path="locationPath"/>
        </div>

        <%--Keywords--%>
        <div class="form-group row">
            <form:label path="keywords" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_KEYWORDS_LABEL"/></form:label>
            <div class="col-md-9">
                <c:set var="placeholder"><op:translate key="SEARCH_FILTERS_KEYWORDS_PLACEHOLDER"/></c:set>
                <form:input path="keywords" cssClass="form-control" placeholder="${placeholder}"/>
            </div>
        </div>

        <%--Size--%>
        <div class="form-group row">
            <form:label path="sizeAmount" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_SIZE_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="sizeRange" cssClass="form-control">
                    <c:forEach var="range" items="${sizeRanges}">
                        <form:option value="${range}"><op:translate key="${range.key}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="col-md">
                <form:input path="sizeAmount" type="number" min="0" step="0.1" cssClass="form-control"/>
            </div>
            <div class="col-md">
                <form:select path="sizeUnit" cssClass="form-control">
                    <c:forEach var="unit" items="${sizeUnits}">
                        <form:option value="${unit}"><op:translate key="${unit.key}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <%--Date--%>
        <div class="form-group row">
            <form:label path="dateRange" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_DATE_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="dateRange" cssClass="form-control" data-change-submit="${namespace}-update">
                    <c:forEach var="range" items="${dateRanges}">
                        <form:option value="${range}"><op:translate key="${range.key}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
            <c:if test="${form.dateRange.customized}">
                <div class="col-md">
                    <form:input path="customizedDate" type="date" cssClass="form-control"/>
                </div>
            </c:if>
        </div>

        <%--Buttons--%>
        <div class="row">
            <div class="col-md-9 offset-md-3">
                <div class="text-right">
                    <c:if test="${not form.modal}">
                        <%--Save search--%>
                        <button type="button" class="btn btn-secondary"
                                data-save-search-popover="${saveSearchPopoverUrl}">
                            <span><op:translate key="SEARCH_FILTERS_SAVE"/></span>
                        </button>
                    </c:if>

                    <c:if test="${form.modal}">
                        <%--Cancel--%>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <span><op:translate key="CANCEL"/></span>
                        </button>
                    </c:if>

                        <%--Submit--%>
                    <button type="submit" name="search" class="btn btn-primary">
                        <span><op:translate key="SEARCH_FILTERS_SUBMIT"/></span>
                    </button>
                </div>
            </div>
        </div>

        <input id="${namespace}-update" type="submit" name="update" class="d-none">
        <input type="submit" name="update-location" class="d-none"/>

        <form:hidden path="savedSearchDisplayName"/>
        <input type="submit" name="save-search-popover-callback" class="d-none"/>
    </form:form>
</div>
