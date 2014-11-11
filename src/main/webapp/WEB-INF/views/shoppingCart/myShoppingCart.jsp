<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Shopping Cart"/>
<%@ include file="../header.jsp" %>

    <div class="page-action">My Shopping Cart</div>

<c:if test="${not empty quantityErrorMessage}">
    <div id="resultMessage" class="page-action error"> ${quantityErrorMessage} </div>
</c:if>

<div class="controls">
    <c:if test="${empty sessionItems}">
        <td>You have no items in your shopping cart</td>
    </c:if>
</div>
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
        <c:forEach var="item" items="${sessionItems}" varStatus="row">
            <tr>
                <td>${item.name}</td>
                <td>${item.description}</td>
                <td>&#163;&nbsp;${item.price}</td>
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
                <c:if test="${not empty sessionItems}">
                    &#163;&nbsp;${totalCartPrice}
                </c:if>
            </td>
        </tr>
    </tbody>
</table>

<table>
    <tr>
        <td>
            <form:form action="/shoppingCart/checkout" method="post" modelAttribute="items">
                <button class="checkout-button" type="submit" name="checkout" id="checkout" value="Checkout" ${empty sessionItems ? 'disabled' : ''}>
                    Checkout
                </button>
            </form:form>
        </td>
        <td></td>
        <td>
            <form:form action="/shoppingCart/clear" method="post" modelAttribute="items">
                <button class="clear-button" type="submit" name="clear" id="clear" value="clear" ${empty sessionItems ? 'disabled' : ''}>
                    Clear Shopping Cart
                </button>
            </form:form>
        </td>
        <td></td>
        <td>
            <form:form action="/" method="get" >
                <button class="continue-button" type="submit" name="continue" id="continueShopping" value="t">
                    Continue Shopping
                </button>
            </form:form>
        </td>

    </tr>
</table>

<%@ include file="../footer.jsp" %>