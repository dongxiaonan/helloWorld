<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Shopping Cart"/>
<%@ include file="../header.jsp" %>

<div class="page-action">My Shopping Cart</div>

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
        <td>${item.name}</td>
        <td>${item.description}</td>
        <td>${item.price}</td>
        <c:if test="${not empty item}">
            <td>1</td>
        </c:if>
    </tr>
    </tbody>
</table>

<form:form action="/shoppingCart/confirmation" method="post" modelAttribute="item">
    <c:if test="${not empty item}">
        <form:hidden path="itemId" value="${item.itemId}"/>
    </c:if>
    <button class="checkout-button" type="submit" name="checkout" id="checkout" value="Checkout">
        Checkout
    </button>
</form:form>

<form:form action="/shoppingCart/myShoppingCart" method="post" modelAttribute="item">
    <button class="clear-button" type="submit" name="clear" id="clear" value="clear">
        Clear Shopping Cart
    </button>
</form:form>

<%@ include file="../footer.jsp" %>