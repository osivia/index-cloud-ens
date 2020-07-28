<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<c:set var="namespace"><portlet:namespace/></c:set>





<form onsubmit="javascript:saveSearch(this); return false;">
    <%--Display name--%>
    <div class="form-group mb-2">
        <label for="${namespace}-save-search-display-name"><op:translate key="SAVED_SEARCH_DISPLAY_NAME"/></label>
        <input id="${namespace}-save-search-display-name" type="text" name="displayName"
               class="form-control form-control-sm">
    </div>

    <div class="text-right">
        <button type="submit" class="btn btn-primary btn-sm">
            <span><op:translate key="SAVE"/></span>
        </button>
    </div>
</form>
