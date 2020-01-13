<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="deleteDiscussion" var="deleteDiscussionUrl" copyCurrentRenderParameters="true"/>
<c:set var="deleteDiscussionConfirmationModalId" value="${namespace}-delete-confirmation"/>


<div class="d-flex flex-column mb-1">
     <div>
        <a href="javascript:" class="btn btn-outline-secondary btn-sm mr-1 no-ajax-link" data-toggle="modal" data-target="#deleteDiscussionConfirmationModalId">
            <i class="glyphicons glyphicons-basic-bin"></i>
            <span class="d-none d-md-inline"><op:translate key="DISCUSSION_DELETE_CURRENT"/></span>
        </a>
     
     </div>
</div>


<div class="portlet-filler d-flex flex-column pr-1">

<c:set var="deleteMessageConfirmationModalId" value="${namespace}-delete-confirmation"/>


<portlet:actionURL name="deleteMessage" var="deleteMessageUrl" copyCurrentRenderParameters="true"/>
<portlet:actionURL name="addMessage" var="addMessageUrl" copyCurrentRenderParameters="true" />


<form:form action="${addMessageUrl}" method="post" modelAttribute="detailForm" cssClass="form-horizontal">


<!-- anchor : ${detailForm.anchor} -->

<c:if test="${not empty detailForm.anchor}">
        <script type="text/javascript">
                setTimeout(function(){ 
                	document.getElementById("${detailForm.anchor}").scrollIntoView();
                }, 100);
        </script>
</c:if>


<div class="discussion-messages">


<c:forEach var="message" items="${detailForm.document.messages}">
    <c:if test="${not message.discussionDeleted}">
       <c:if test="${detailForm.author eq message.author}">
		  <div class="row ">
	                <div class="col-10">
	                         <div class="text-right text-muted small mb-1">
	                               <fmt:formatDate value="${message.date}" type="date" dateStyle="long"/>  
	                         
		                         <c:if test="${not message.deleted}">
		 	                        <span>
		                                <%--Delete--%>
			                         <a href="javascript:" style="margin-top: -6px" class="btn btn-sm ml-1 no-ajax-link glyphicons glyphicons-basic-bin text-muted float-right" data-toggle="modal"
			                            data-target="#${deleteMessageConfirmationModalId}"
			                            data-message-id="${message.id}">
			                             
			                         </a>
			                        </span> 
                        
		                        </c:if>
                                </div>	
                               <c:if test="${not message.deleted}">
                                        <div class="border rounded mb-2 p-3 bg-info text-white">
                                                ${message.content}
                                        </div>                          
                                </c:if>
	                       <c:if test="${message.deleted}">
	                                <div class="border rounded mb-2 p-3 bg-info text-white">
	                                        <span><op:translate  key="DISCUSSION_MESSAGE_DELETED"/></span>
	                                </div>                                 
	                        </c:if> 
	                </div>
	        </div>
        </c:if>
        
         <c:if test="${detailForm.author ne message.author}">
                <div class="row ">
                        <div class="offset-2  col-10">
                                 <div class="text-right text-muted small mb-1">
                                        <fmt:formatDate value="${message.date}" type="date" dateStyle="long"/> - <ttc:user name="${message.author}" linkable="false"/> 
                                     
                                 </div>                                        
                                 <c:if test="${not message.deleted}">
                                        <div class="border rounded mb-2 p-3 bg-light">
                                                ${message.content}
                                        </div>                          
                                </c:if>
                               <c:if test="${message.deleted}">
                                        <div class="border rounded mb-2 p-3 bg-light">
                                                <span class="text-muted"><op:translate  key="DISCUSSION_MESSAGE_DELETED"/></span>
                                        </div>                                 
                                </c:if> 
                                
                        </div>
                </div>
        </c:if>
        
    </c:if>
        
</c:forEach>
	
	<div class="row ">
                <div class="col-10 text-right">
                        <div class="mt-2">
                                <c:set var="placeholder"><op:translate key="DISCUSSION_NEW_MESSAGE_PLACEHOLDER" /></c:set>
                                <form:textarea path="newMessage" rows="3" cssClass="form-control" placeholder="${placeholder}" />
                         </div>
                </div>
                
                <div class="col-2 d-flex align-items-center pl-0">
                        <div class="m-auto">
                        
                        <button type="submit" class="btn btn-primary glyphicons glyphicons glyphicons-basic-send">
                                <span class="d-none d-lg-inline"><op:translate key="DISCUSSION_SEND" /></span>
                         </button>

                        </div>
                </div>
        </div>
        

</form:form>





    <%--Delete modal confirmation--%>
    <div id="${deleteMessageConfirmationModalId}" class="modal fade" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><op:translate
                            key="DISCUSSION_DELETE_MESSAGE_CONFIRMATION_MODAL_BODY"/></h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <p><op:translate key="DISCUSSION_DELETE_MESSAGE_CONFIRMATION_MODAL_DETAIL"/></p>
                    <form action="${deleteMessageUrl}" method="post">
                        <input type="hidden" name="messageId">
                        <input type="submit" class="d-none">
                    </form>
                </div>

                <div class="modal-footer">
                    <%--Cancel--%>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>

                    <%--Delete--%>
                    <button type="button" class="btn btn-primary" data-submit>
                        <span><op:translate key="DISCUSSION_DELETE"/></span>
                    </button>
                </div>
            </div>
        </div>
    </div>


    <!-- Delete discussion modal -->
    <div id="deleteDiscussionConfirmationModalId" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">

                <div class="modal-header">
                    <h5 class="modal-title"><op:translate
                            key="DISCUSSION_DELETE_CURRENT_MODAL_BODY"/></h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <p><op:translate key="DISCUSSION_DELETE_CURRENT_MODAL_MESSAGE"/></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>

                    <a href="${deleteDiscussionUrl}" class="btn btn-primary" data-dismiss="modal">
                        <span><op:translate key="DISCUSSION_DELETE"/></span>
                    </a>
                </div>
            </div>
        </div>
    </div>

</div>

</div>

