<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<portlet:actionURL name="save" var="saveUrl" />

WSWS

<form:form action="${saveUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
    <fieldset>
        <legend><op:translate key="SIMPLE_DOCUMENT_CREATION_LEGEND"/></legend>
    
        <!-- Document parent path -->
        <spring:bind path="parentPath">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="parentPath" cssClass="col-sm-3 control-label"><op:translate key="SIMPLE_DOCUMENT_CREATION_PARENT_PATH"/></form:label>
                <div class="col-sm-9">
                    <form:input path="parentPath" cssClass="form-control"/>
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="parentPath" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
    
        <!-- Document type -->
        <spring:bind path="type">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="type" cssClass="col-sm-3 control-label"><op:translate key="SIMPLE_DOCUMENT_CREATION_TYPE"/></form:label>
                <div class="col-sm-9">
                    <form:input path="type" cssClass="form-control"/>
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="type" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
    
        <!-- Document title -->
        <spring:bind path="title">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="title" cssClass="col-sm-3 control-label"><op:translate key="SIMPLE_DOCUMENT_CREATION_TITLE"/></form:label>
                <div class="col-sm-9">
                    <form:input path="title" cssClass="form-control"/>
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="title" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
        
        <!-- Document description -->
        <div class="form-group">
            <form:label path="description" cssClass="col-sm-3 control-label"><op:translate key="SIMPLE_DOCUMENT_CREATION_DESCRIPTION"/></form:label>
            <div class="col-sm-9">
                <form:textarea path="description" cssClass="form-control"/>
            </div>
        </div>
    
        <!-- Buttons -->
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <!-- Save -->
                <button type="submit" name="save" class="btn btn-primary">
                    <i class="glyphicons glyphicons-floppy-disk"></i>
                    <span><op:translate key="SAVE"/></span>
                </button>
                
                <!-- Randomize -->
                <button type="submit" name="randomize" class="btn btn-default">
                    <i class="glyphicons glyphicons-playing-dices"></i>
                    <span><op:translate key="SIMPLE_DOCUMENT_CREATION_RANDOMIZE"/></span>
                </button>
            </div>
        </div>
    </fieldset>
</form:form>
