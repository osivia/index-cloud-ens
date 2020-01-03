<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<c:set var="deleteMessageConfirmationModalId" value="${namespace}-delete-confirmation"/>


<portlet:actionURL name="deleteMessage" var="deleteMessageUrl" copyCurrentRenderParameters="true"/>
<portlet:actionURL name="addMessage" var="addMessageUrl" copyCurrentRenderParameters="true" />

<!-- anchor : ${detailForm.anchor} -->

<c:if test="${not empty detailForm.anchor}">
        <script type="text/javascript">
                setTimeout(function(){ location.href = "#${detailForm.anchor}"; }, 100);
        </script>
</c:if>

<form:form action="${addMessageUrl}" method="post" modelAttribute="detailForm" cssClass="form-horizontal">


<div class="discussion-messages">

<!-- 
	<div class="row ">
		<div class=" col-10">
			<span class="text-right text-muted">Le ..... par </span>
			<div class="border rounded mb-2 p-3 bg-light">qsdflmkqsfmlkqsfmlk sfljsdflkjsdflkj sdflkjsfdlkj sdflkjsdfjl
				sldflkj</div>
		</div>
	</div>
	<div class="row ">
		<div class="offset-2  col-10">
			<div class="text-right">
				Le ..... 12/11/2019 <i class="glyphicons glyphicons-basic-bin"></i> <span class="d-none">Supprimer</span>
			</div>
			<div class="border rounded mb-2  p-3 bg-info text-white">sdqsdqsd nqsfd;kjhsdflkhsdf sdflkhsdfkjhsdfkjhsdfkjh
				sdfkjhsdfkh qsdflmkqsfmlkqsfmlk sfljsdflkjsdflkj sdflkjsfdlkj sdflkjsdfjl sldflkj</div>
		</div>
	</div>

	<div class="row ">
		<div class="col-10">
			<span class="text-right">Le ..... par </span>
			<div class="border rounded mb-2 p-3 bg-light">qsdflmkqsfmlkqsfmlk sfljsdflkjsdflkj sdflkjsfdlkj sdflkjsdfjl
				sldflkj</div>
		</div>
	</div>


	<div class="row ">
		<div class="col-10 text-right">
			<div class="mt-2">
				<textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
			</div>
		</div>
		
		<div class="col-2 d-flex align-items-center pl-0">
			<div class="m-auto">
				<a class="btn btn-primary no-ajax-link" href="/toutatice-portail-cms-nuxeo/binary/pres232.pptx?type=FILE&amp;path=%2Fdefault-domain%2FUserWorkspaces%2Fd%2Fe%2Fm%2Fdemo%2Fdocuments%2Fpres-pptx&amp;portalName=default&amp;fieldName=file:content&amp;t=1576776759728&amp;reload=true"
					title="Téléchargement"> <i class="glyphicons glyphicons glyphicons-basic-send"></i> <span
					class="d-none d-lg-inline">Envoyer</span>
				</a>
			</div>
		</div>
	</div>
	
	
	<ttc:user name="${message.author}" linkable="false"/> / 
	
-->

<c:forEach var="message" items="${detailForm.document.messages}">


        
        
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
        
         <c:if test="${detailForm.author ne message.author}">
                <div class="row ">
                        <div class="offset-2  col-10">
                                 <div class="text-right text-muted small mb-1">
                                        <fmt:formatDate value="${message.date}" type="date" dateStyle="long"/> - <ttc:user name="${message.author}" linkable="false"/> 
                                     
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


</div>

