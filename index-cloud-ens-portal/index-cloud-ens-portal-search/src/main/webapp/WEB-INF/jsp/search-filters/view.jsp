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
<portlet:resourceURL id="load-vocabulary" var="loadFileFormatsUrl">
    <portlet:param name="vocabulary" value="idx_file_format"/>
</portlet:resourceURL>
<portlet:resourceURL id="load-vocabulary" var="loadSharedUrl">
    <portlet:param name="vocabulary" value="idx_shared"/>
</portlet:resourceURL>
<portlet:resourceURL id="save-search-popover" var="saveSearchPopoverUrl"/>


<c:set var="namespace"><portlet:namespace/></c:set>

<c:set var="select2Searching"><op:translate key="SELECT2_SEARCHING"/></c:set>
<c:set var="select2NoResults"><op:translate key="SELECT2_NO_RESULTS"/></c:set>


<div class="search-filters-container">
    <%--@elvariable id="form" type="fr.index.cloud.ens.search.filters.portlet.model.SearchFiltersForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="form">
        <%--Keywords--%>
        <div class="form-group row">
            <form:label path="keywords" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_KEYWORDS_LABEL"/></form:label>
            <div class="col-md-9">
                <div class="input-group">
                    <c:set var="placeholder"><op:translate key="SEARCH_FILTERS_KEYWORDS_PLACEHOLDER"/></c:set>
                    <form:input path="keywords" cssClass="form-control" placeholder="${placeholder}"/>
                    <div class="input-group-append">
                        <span class="input-group-text">
                            <i class="glyphicons glyphicons-basic-search"></i>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <%--Document types--%>
        <div class="form-group row">
            <form:label path="documentTypes" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_DOCUMENT_TYPE_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="documentTypes" cssClass="form-control select2 select2-default" data-url="${loadDocumentTypesUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                    <c:forEach var="documentType" items="${form.documentTypes}">
                        <form:option value="${documentType}"><ttc:vocabularyLabel name="idx_document_type" key="${documentType}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <%--Levels--%>
        <div class="form-group row">
            <form:label path="levels" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_LEVEL_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="levels" cssClass="form-control select2 select2-default" data-url="${loadLevelsUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                    <c:forEach var="level" items="${form.levels}">
                        <form:option value="${level}"><ttc:vocabularyLabel name="idx_level" key="${level}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <%--Subjects--%>
        <div class="form-group row">
            <form:label path="subjects" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_SUBJECT_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="subjects" cssClass="form-control select2 select2-default" data-url="${loadSubjectsUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                    <c:forEach var="subject" items="${form.subjects}">
                        <form:option value="${subject}"><ttc:vocabularyLabel name="idx_subject" key="${subject}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <%--Location--%>
        <c:if test="${form.view.id eq 'default'}">
            <div class="form-group row">
                <form:label path="location" cssClass="col-md-3 col-form-label"><op:translate
                        key="SEARCH_FILTERS_LOCATION_LABEL"/></form:label>
                <div class="col-md-6">
                    <a href="javascript:" class="btn btn-link btn-link-hover-primary text-primary-dark bg-white" data-target="#osivia-modal" data-load-url="${locationUrl}" data-size="small">
                        <strong><ttc:title document="${form.location}" linkable="false" icon="true"/></strong>
                    </a>
                </div>
                <form:hidden path="locationPath"/>
            </div>
        </c:if>

        <%--Format--%>
        <div class="form-group row">
            <form:label path="format" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_FORMAT_LABEL"/></form:label>
            <div class="col-md-6">
                <form:select path="format" cssClass="form-control select2 select2-default" data-url="${loadFileFormatsUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
                    <c:forEach var="format" items="${form.format}">
                        <form:option value="${format}"><ttc:vocabularyLabel name="idx_file_format" key="${format}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>      
        
        <%--Partage--%>
        <c:if test="${form.view.id eq 'default'}">         
	        <div class="form-group row">
	            <form:label path="shared" cssClass="col-md-3 col-form-label"><op:translate
	                    key="SEARCH_FILTERS_SHARED_LABEL"/></form:label>
	            <div class="col-md-6">
	                <form:select path="shared" cssClass="form-control select2 select2-default" data-url="${loadSharedUrl}" data-searching="${select2Searching}" data-no-results="${select2NoResults}">
	                    <c:forEach var="shared" items="${form.shared}">
	                        <form:option value="${format.shared}"><ttc:vocabularyLabel name="idx_shared" key="${shared}"/></form:option>
	                    </c:forEach>
	                </form:select>
	            </div>
	        </div>       
        </c:if>               

        <%--Size--%>
        <div class="form-group row">
            <form:label path="sizeAmount" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_SIZE_LABEL"/></form:label>
            <div class="col-md-9">
                <div class="form-row">
                    <div class="col-md-8">
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
            </div>
        </div>

        <%--Date--%>
        <div class="form-group row">
            <form:label path="dateRange" cssClass="col-md-3 col-form-label"><op:translate
                    key="SEARCH_FILTERS_DATE_LABEL"/></form:label>
            <div class="col-md-9">
                <div class="form-row">
                    <div class="col-md-8">
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
            </div>
        </div>

        <%--Buttons--%>
        <div class="row">
            <div class="col-md-9 offset-md-3">
                <div class="text-right">
                    <%--Save search--%>
                    <button type="button" class="btn btn-secondary"
                            data-save-search-popover="${saveSearchPopoverUrl}">
                        <span><op:translate key="SEARCH_FILTERS_SAVE"/></span>
                    </button>

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
