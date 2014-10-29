<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Shopping Cart"/>
<%@ include file="../header.jsp" %>

<script type="text/javascript">
    $(function () {
        new Survey().showSurvey(new SurveyPopUp());
    })
</script>

<div class="page-action">Order has been placed successfully</div>

<table class="table">
    <thead>
    <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Quantity</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <c:if test="${not empty item}">
            <td>${item.name}</td>
            <td>${item.description}</td>
            <td>${item.price}</td>
            <td>1</td>
        </c:if>
    </tr>
    </tbody>
</table>
<hr>
<table>
    <tbody>
    <%--<tr>--%>
        <%--<td>--%>
            <%--Tax:--%>
        <%--</td>--%>
        <%--<td>--%>
            <%--0--%>
        <%--</td>--%>
    <%--</tr>--%>
    <tr>
        <td>
            Total:
        </td>
        <td>
            ${item.price}
        </td>
    </tr>
    </tbody>
</table>

<%@ include file="../footer.jsp" %>