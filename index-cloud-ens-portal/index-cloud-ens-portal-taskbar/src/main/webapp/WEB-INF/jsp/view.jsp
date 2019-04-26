<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="taskbar">
    <ul class="list-unstyled">
        <c:forEach var="task" items="${taskbar.tasks}">
            <li>
                <a href="${task.url}" class="d-block py-2 px-3 ${task.active ? 'text-primary font-weight-bold' : 'text-muted'} text-decoration-none no-ajax-link">
                    <i class="${task.icon}"></i>
                    <span>${task.displayName}</span>
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
