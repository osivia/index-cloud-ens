<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="submitForm" var="submitFormUrl"/>

<portlet:resourceURL id="password-information" var="passwordInformationUrl"/>


<div class="create-account">
    <div class="row">
        <div class="col-lg-8">
            <div class="card bg-blue-lighter shadow-lg create-account">
                <div class="card-body">
                    <h3 class="card-title text-center text-uppercase font-weight-bold"><op:translate key="createaccount.title"/></h3>

                    <form:form action="${submitFormUrl}" method="post" modelAttribute="form" role="form">
                        <div class="row">
                            <div class="col-md">
                                <%--Nickname--%>
                                <spring:bind path="nickname">
                                    <div class="form-group required">
                                        <form:label path="nickname" cssClass="text-secondary"><op:translate key="createaccount.form.nickname"/></form:label>
                                        <form:input path="nickname" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                        <form:errors path="nickname" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>

                                <%--Lastname--%>
                                <spring:bind path="lastname">
                                    <div class="form-group required">
                                        <form:label path="lastname" cssClass="text-secondary"><op:translate key="createaccount.form.lastname"/></form:label>
                                        <form:input path="lastname" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                        <form:errors path="lastname" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>

                                <%--Firstname--%>
                                <spring:bind path="firstname">
                                    <div class="form-group required">
                                        <form:label path="firstname" cssClass="text-secondary"><op:translate key="createaccount.form.firstname"/></form:label>
                                        <form:input path="firstname" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                        <form:errors path="firstname" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>
                            </div>

                            <div class="col-md">
                                <%--Mail--%>
                                <spring:bind path="mail">
                                    <div class="form-group required">
                                        <form:label path="mail" cssClass="text-secondary"><op:translate key="createaccount.form.mail"/></form:label>
                                        <form:input path="mail" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                        <form:errors path="mail" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>

                                <%--Password--%>
                                <spring:bind path="newpassword">
                                    <div class="form-group required">
                                        <div class="mb-2">
                                            <form:label path="newpassword" cssClass="text-secondary"><op:translate key="createaccount.form.newpassword"/></form:label>
                                            <form:password path="newpassword" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                            <form:errors path="newpassword" cssClass="invalid-feedback"/>
                                        </div>

                                        <div data-password-information-placeholder data-url="${passwordInformationUrl}"></div>
                                    </div>
                                </spring:bind>

                                <%--Password confirmation--%>
                                <spring:bind path="confirmpassword">
                                    <div class="form-group required">
                                        <form:label path="confirmpassword" cssClass="text-secondary"><op:translate key="createaccount.form.confirmpassword"/></form:label>
                                        <form:password path="confirmpassword" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                        <form:errors path="confirmpassword" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>
                            </div>
                        </div>

                        <%--Buttons--%>
                        <div class="text-right">
                            <button type="submit" name="save" class="btn btn-secondary">
                                <span class="text-uppercase font-weight-bold"><op:translate key="createaccount.submit"/></span>
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

        <div class="col-lg-4 d-flex flex-column py-4">
            <div class="card bg-blue-lighter rounded-0 my-auto">
                <div class="card-body">
                    <%--RGPD--%>
                    <div class="d-flex justify-content-end">
                        <h2 class="d-flex align-items-center h4 bg-blue-dark py-1 px-2 mb-3">
                            <span class="text-blue-light font-weight-bolder mr-2">RGPD</span>
                            <span class="text-white">
                                <i class="glyphicons glyphicons-basic-shield-check"></i>
                            </span>
                        </h2>
                    </div>

                    <p class="mb-1">Utiliser le Cloud PRONOTE c'est&nbsp;:</p>
                    <ul class="pl-3">
                        <li class="mb-1">avoir la garantie de rester propri&eacute;taire de vos documents</li>
                        <li class="mb-1">pouvoir cl&ocirc;turer votre compte &agrave; tout moment</li>
                        <li class="mb-1">&ecirc;tre assur&eacute; que vos donn&eacute;es personnelles ne seront pas exploit&eacute;es</li>
                        <li class="mb-1">pouvoir r&eacute;cup&eacute;rer tout ou partie de vos documents quand vous le souhaitez</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
