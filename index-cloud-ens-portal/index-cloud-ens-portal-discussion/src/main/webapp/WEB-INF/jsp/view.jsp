<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="delete-all" var="deleteAllUrl"/>
<portlet:actionURL name="new-discussion-users" var="newDiscussionUsersUrl"/>
<portlet:actionURL name="new-discussion-publication" var="newDiscussionPublicationUrl"/>

<c:set var="namespace"><portlet:namespace/></c:set>


<div class="trash">
    <%@ include file="table.jspf" %>
   
    
    <!--new discussion modal -->
    <div id="${namespace}-new-discussion-users" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <span><op:translate key="DISCUSSION_NEW"/></span>
                    </h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

             <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>

                    <a href="${newDiscussionUsersUrl}" class="btn btn-danger" data-dismiss="modal">
                        <span><op:translate key="DISCUSSION_NEW"/></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
    
        <!--new discussion modal -->
    <div id="${namespace}-new-discussion-publication" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <span><op:translate key="DISCUSSION_NEW_PUB"/></span>
                    </h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

             <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>

                    <a href="${newDiscussionPublicationUrl}" class="btn btn-danger" data-dismiss="modal">
                        <span><op:translate key="DISCUSSION_NEW"/></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    
</div>
