<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>


<portlet:actionURL name="cancel-inline-edition" var="cancelUrl"/>

<c:set var="namespace"><portlet:namespace/></c:set>


<div class="metadata">
    <div class="card mb-3">
        <div class="card-body">
            <%--Mutualized title--%>
            <c:if test="${readOnly}">
                <h3 class="h4 card-title">${document.properties['mtz:title']}</h3>
            </c:if>

            <%--Keywords--%>
            <c:if test="${readOnly and not empty document.properties['mtz:keywords']}">
                <p class="card-text mb-0"><op:translate key="DOCUMENT_MUTUALIZATION_KEYWORDS"/></p>
                <ul class="list-inline">
                    <c:forEach var="keyword" items="${document.properties['mtz:keywords']}">
                        <li class="list-inline-item">${keyword}</li>
                    </c:forEach>
                </ul>
            </c:if>

            <%--Levels--%>
            <c:set var="levels" value="${document.properties['idxcl:levels']}"/>
            <c:choose>
                <c:when test="${readOnly}">
                    <p class="card-text mb-0"><op:translate key="DOCUMENT_METADATA_LEVEL_LABEL"/></p>
                    <ul class="list-inline">
                        <c:forEach var="level" items="${levels}">
                            <li class="list-inline-item"><ttc:vocabularyLabel name="idx_level" key="${level}"/></li>
                        </c:forEach>
                    </ul>
                </c:when>

                <c:otherwise>
                    <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_LEVEL_PLACEHOLDER"/></c:set>
                    <portlet:actionURL name="inline-edition" var="submitUrl">
                        <portlet:param name="property" value="idxcl:levels"/>
                        <portlet:param name="cancel-url" value="${cancelUrl}"/>
                    </portlet:actionURL>
                    <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                        <portlet:param name="vocabulary" value="idx_level"/>
                        <portlet:param name="optgroupDisabled" value="true"/>
                    </portlet:resourceURL>
                    <form action="${submitUrl}" method="post">
                        <div class="form-group inline-edition">
                            <label for="${namespace}-levels"><op:translate key="DOCUMENT_METADATA_LEVEL_LABEL"/></label>
                            <select id="${namespace}-levels" name="inline-values" multiple="multiple"
                                    class="form-control select2 select2-inline-edition" data-url="${select2Url}"
                                    data-placeholder="${placeholder}">
                                <c:forEach var="level" items="${levels}">
                                    <option value="${level}" selected="selected"><ttc:vocabularyLabel name="idx_level"
                                                                                                      key="${level}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <input type="submit" class="d-none">
                    </form>
                </c:otherwise>
            </c:choose>

            <%--Subjects--%>
            <c:set var="subjects" value="${document.properties['idxcl:subjects']}"/>
            <c:choose>
                <c:when test="${readOnly}">
                    <p class="card-text mb-0"><op:translate key="DOCUMENT_METADATA_SUBJECT_LABEL"/></p>
                    <ul class="list-inline">
                        <c:forEach var="subject" items="${subjects}">
                            <li class="list-inline-item"><ttc:vocabularyLabel name="idx_subject" key="${subject}"/></li>
                        </c:forEach>
                    </ul>
                </c:when>

                <c:otherwise>
                    <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_SUBJECT_PLACEHOLDER"/></c:set>
                    <portlet:actionURL name="inline-edition" var="submitUrl">
                        <portlet:param name="property" value="idxcl:subjects"/>
                        <portlet:param name="cancel-url" value="${cancelUrl}"/>
                    </portlet:actionURL>
                    <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                        <portlet:param name="vocabulary" value="idx_subject"/>
                        <portlet:param name="optgroupDisabled" value="true"/>
                    </portlet:resourceURL>
                    <form action="${submitUrl}" method="post">
                        <div class="form-group inline-edition">
                            <label for="${namespace}-subjects"><op:translate
                                    key="DOCUMENT_METADATA_SUBJECT_LABEL"/></label>
                            <select id="${namespace}-subjects" name="inline-values" multiple="multiple"
                                    class="form-control select2 select2-inline-edition" data-url="${select2Url}"
                                    data-placeholder="${placeholder}">
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject}" selected="selected"><ttc:vocabularyLabel
                                            name="idx_subject"
                                            key="${subject}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <input type="submit" class="d-none">
                    </form>
                </c:otherwise>
            </c:choose>

            <%--Document types--%>
            <c:set var="documentTypes" value="${document.properties['idxcl:documentTypes']}"/>
            <c:choose>
                <c:when test="${readOnly}">
                    <p class="card-text mb-0"><op:translate key="DOCUMENT_METADATA_DOCUMENT_TYPE_LABEL"/></p>
                    <ul class="list-inline">
                        <c:forEach var="documentType" items="${documentTypes}">
                            <li class="list-inline-item"><ttc:vocabularyLabel name="idx_document_type"
                                                                              key="${documentType}"/></li>
                        </c:forEach>
                    </ul>
                </c:when>

                <c:otherwise>
                    <c:set var="placeholder"><op:translate key="DOCUMENT_METADATA_DOCUMENT_TYPE_PLACEHOLDER"/></c:set>
                    <portlet:actionURL name="inline-edition" var="submitUrl">
                        <portlet:param name="property" value="idxcl:documentTypes"/>
                        <portlet:param name="cancel-url" value="${cancelUrl}"/>
                    </portlet:actionURL>
                    <portlet:resourceURL id="select2-vocabulary" var="select2Url">
                        <portlet:param name="vocabulary" value="idx_document_type"/>
                        <portlet:param name="optgroupDisabled" value="true"/>
                    </portlet:resourceURL>
                    <form action="${submitUrl}" method="post">
                        <div class="form-group inline-edition">
                            <label for="${namespace}-document-types"><op:translate
                                    key="DOCUMENT_METADATA_DOCUMENT_TYPE_LABEL"/></label>
                            <select id="${namespace}-document-types" name="inline-values" multiple="multiple"
                                    class="form-control select2 select2-inline-edition" data-url="${select2Url}"
                                    data-placeholder="${placeholder}">
                                <c:forEach var="documentType" items="${documentTypes}">
                                    <option value="${documentType}" selected="selected"><ttc:vocabularyLabel
                                            name="idx_document_type" key="${documentType}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <input type="submit" class="d-none">
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>


    <c:if test="${not readOnly}">
        <div class="card mb-3">
            <div class="card-body">
                <c:set var="share" value="${document.properties['rshr:linkId']}"/>
                <c:set var="enabled" value="${document.properties['rshr:enabledLink']}"/>
                <c:set var="targets" value="${document.properties['rshr:targets']}"/>

                <c:if test="${not empty targets}">
                    <p>
                        <span class="badge badge-warning">${fn:length(targets)}</span>
                        <a href="#" class="no-ajax-link " data-toggle="modal" data-target="#${namespace}-targets">
                            <c:choose>
                                <c:when test="${fn:length(targets) eq 1}">
                                    <op:translate key="SHARED_TARGET_REFERENCE"/>
                                </c:when>
                                <c:otherwise>
                                    <op:translate key="SHARED_TARGET_REFERENCES"/>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </p>

                    <%--Target modal--%>
                    <div id="${namespace}-targets" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        <span><op:translate key="SHARED_TARGET_TITLE"/></span>
                                    </h5>

                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>

                                <div class="modal-body">
                                    <table class="table">
                                        <thead class="thead-light">
                                        <tr>
                                            <th scope="col"><op:translate
                                                    key="SHARED_TARGET_REFERENCES_ORGANIZATION"/></th>
                                            <th scope="col"><op:translate key="SHARED_TARGET_REFERENCES_DATE"/></th>
                                            <th scope="col"><op:translate key="SHARED_TARGET_REFERENCES_GROUP"/></th>
                                            <th scope="col"><op:translate key="SHARED_TARGET_REFERENCES_CONTEXT"/></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="target" items="${targets}">
                                            <c:set var="pubOrganization" value="${target.pubOrganization}"/>
                                            <c:set var="pubDate" value="${target.pubDate}"/>
                                            <c:set var="pubGroup" value="${target.pubGroup}"/>
                                            <c:set var="pubContext" value="${target.pubContext}"/>

                                            <tr>
                                                <td><c:if test="${not empty pubOrganization}">
                                                    <op:oauth2ClientDetail clientId="${pubOrganization}" var="client"/>
                                                    ${client.title}
                                                </c:if></td>
                                                <td><c:if test="${not empty pubDate}">
                                                    <fmt:formatDate value="${pubDate}" type="date" dateStyle="long"/>
                                                </c:if></td>
                                                <td><c:if test="${not empty pubGroup}">
                                                    ${target.pubGroup}
                                                </c:if></td>
                                                <td><c:if test="${not empty pubContext}">
                                                    ${target.pubContext}
                                                </c:if></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>


                <c:if test="${enabled}">
                    <div class="d-flex">
                        <div>
                            <span>
                                <i class="glyphicons glyphicons-link"></i>
                            </span>
                        </div>
                        <div>
                            <span>
                               <a href="/s/${share}"><op:translate key="SHARED_LINK"/></a>
                            </span>


                            <%--Format--%>
                            <c:if test="${document.pdfConvertible}">
                                <portlet:actionURL name="inline-edition" var="submitUrl">
                                    <portlet:param name="property" value="rshr:format"/>
                                    <portlet:param name="cancel-url" value="${cancelUrl}"/>
                                </portlet:actionURL>
                                <form action="${submitUrl}" method="post">
                                    <c:if test="${not empty targets}">
                                        <input type="hidden" name="warn-message"
                                               value="<op:translate key="DOCUMENT_CHANGE_FORMAT_WARN_MESSAGE"/>">
                                    </c:if>

                                    <div class="form-group inline-edition">
                                        <label class="control-label"><op:translate
                                                key="DOCUMENT_METADATA_FORMAT_LABEL"/></label>
                                        <div class="radio">
                                            <label> <input type="radio" name="inline-values"
                                                           value="pdf" ${empty document.properties['rshr:format'] or document.properties['rshr:format'] eq 'pdf' ? 'checked' : ''}>
                                                <op:translate key="SHARED_FORMAT_PDF"/>
                                            </label>
                                        </div>
                                        <div class="radio">
                                            <label> <input type="radio" name="inline-values"
                                                           value="native" ${document.properties['rshr:format'] eq 'native' ? 'checked' : ''}>
                                                <op:translate key="SHARED_FORMAT_NATIVE"/>
                                            </label>
                                        </div>
                                    </div>

                                    <input type="submit" class="d-none">
                                </form>
                            </c:if>

                        </div>
                    </div>


                    <div class="d-flex">
                        <portlet:actionURL name="link-activation" var="deactivationUrl">
                            <portlet:param name="activate" value="false"/>
                        </portlet:actionURL>
                        <span>
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                        <a href="${deactivationUrl}"><op:translate key="SHARED_LINK_DEACTIVATE"/></a>
                    </div>
                </c:if>

                <c:if test="${not enabled}">
                    <portlet:actionURL name="link-activation" var="activationUrl">
                        <portlet:param name="activate" value="true"/>
                    </portlet:actionURL>

                    <div class="d-flex">
                        <span>
                            <i class="glyphicons glyphicons-link"></i>
                        </span>
                        <span>
                           <a href="${activationUrl}"><op:translate key="SHARED_LINK_ACTIVATE"/></a>
                         </span>
                    </div>
                </c:if>
            </div>
        </div>
    </c:if>


    <%--Mutualized informations--%>
    <c:if test="${not readOnly and not empty document.publishedDocuments}">
        <div class="card mb-3 bg-mutualized-lighter border-mutualized-dark">
            <div class="card-body">
                    <%--Title--%>
                <p class="card-text mb-0"><op:translate key="DOCUMENT_MUTUALIZATION_TITLE"/></p>
                <p class="card-text">
                    <strong>${document.properties['mtz:title']}</strong>
                </p>

                    <%--Keywords--%>
                <p class="card-text mb-0"><op:translate key="DOCUMENT_MUTUALIZATION_KEYWORDS"/></p>
                <c:choose>
                    <c:when test="${empty document.properties['mtz:keywords']}">
                        <p class="card-text">&ndash;</p>
                    </c:when>

                    <c:otherwise>
                        <ul class="list-inline">
                            <c:forEach var="keyword" items="${document.properties['mtz:keywords']}">
                                <li class="list-inline-item">${keyword}</li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>

                    <%--Desynchronized indicator--%>
                <c:if test="${desynchronized}">
                    <p class="card-text text-mutualized-dark"><op:translate
                            key="DOCUMENT_MUTUALIZATION_DESYNCHRONIZED"/></p>
                </c:if>

                <ul class="card-text list-inline">
                        <%--Views--%>
                    <li class="list-inline-item">
                        <i class="glyphicons glyphicons-basic-eye"></i>
                        <strong>${document.properties['mtz:views']}</strong>
                        <c:choose>
                            <c:when test="${empty document.properties['mtz:views'] or document.properties['mtz:views'] le 1}">
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_VIEW"/></span>
                            </c:when>
                            <c:otherwise>
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_VIEWS"/></span>
                            </c:otherwise>
                        </c:choose>
                    </li>

                        <%--Downloads--%>
                    <li class="list-inline-item">
                        <i class="glyphicons glyphicons-basic-save"></i>
                        <strong>${document.properties['mtz:downloads']}</strong>
                        <c:choose>
                            <c:when test="${empty document.properties['mtz:downloads'] or document.properties['mtz:downloads'] le 1}">
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_DOWNLOAD"/></span>
                            </c:when>
                            <c:otherwise>
                                <span><op:translate key="DOCUMENT_MUTUALIZATION_DOWNLOADS"/></span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>

                <a href="#" class="card-link text-mutualized-dark">
                    <span><op:translate key="DOCUMENT_MUTUALIZATION_CONTACT_READERS"/></span>
                </a>
            </div>
        </div>
    </c:if>


    <c:if test="${not readOnly and not empty document.properties['mtz:sourceWebId']}">
        <div class="card mb-3 bg-mutualized-lighter border-mutualized-dark">
            <div class="card-body">
                <p class="card-text"><op:translate key="DOCUMENT_COPIED_INFORMATION"/></p>

                <p class="card-text">
                    <a href="#" class="text-mutualized-dark">
                        <span><op:translate key="DOCUMENT_COPIED_CONTACT_AUTHOR"/></span>
                    </a>
                </p>

                <c:choose>
                    <c:when test="${empty source}">
                        <p class="card-text text-muted"><op:translate key="DOCUMENT_COPIED_SOURCE_NOT_FOUND"/></p>
                    </c:when>

                    <c:otherwise>
                        <ttc:documentLink document="${source}" var="sourceLink" permalink="true"/>
                        <p class="card-text">
                            <a href="${sourceLink.url}" class="text-mutualized-dark no-ajax-link">
                                <span><op:translate key="DOCUMENT_COPIED_VIEW_SOURCE"/></span>
                            </a>
                        </p>

                        <c:if test="${document.properties['mtz:sourceVersion'] ne source.document.versionLabel}">
                            <p class="card-text text-muted">
                                <small><op:translate key="DOCUMENT_COPIED_NEW_VERSION_FOUND"/></small>
                            </p>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>
</div>
