<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Shopping Cart"/>
<%@ include file="../header.jsp" %>

<script type="text/javascript">
    $(function () {
        new Survey().showSurvey(new SurveyPopUp());
    })
</script>

<div class="page-action" id="confirmationMessage">Order has been placed successfully. Thank you for your purchase!</div>

<div class="page-action" id="shippingAddress">
    Shipping Address
</div>

<br/> ${address}

<div class="page-action" id="orderSummary">Order Summary</div>
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
        <c:forEach var="item" items="${items}" varStatus="row">
            <tr>
                <td>${item.name}</td>
                <td>${item.description}</td>
                <td>&pound; ${item.price}</td>
                <td>${item.quantity}</td>
            </tr>
        </c:forEach>
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
           &pound; ${totalPrice}
        </td>
    </tr>
    </tbody>
</table>

<%@ include file="../footer.jsp" %>