<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Shopping Cart"/>
<%@ include file="../header.jsp" %>

<div class="page-action">My Shopping Cart</div>
<div class="controls">
    <c:if test="${empty sessionItem}">
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
        <tr>
            <c:if test="${not empty sessionItem}">
                <td>${sessionItem.name}</td>
                <td>${sessionItem.description}</td>
                <td>${sessionItem.price}</td>
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
                <c:if test="${not empty sessionItem}">
                    ${sessionItem.price}
                </c:if>
            </td>
        </tr>
    </tbody>
</table>

<table>
    <tr>
        <td>
            <form:form action="/shoppingCart/confirmation" method="post" modelAttribute="item">
                <c:if test="${not empty item}">
                    <form:hidden path="itemId" value="${sessionItem.itemId}"/>
                </c:if>
                <button class="checkout-button" type="submit" name="checkout" id="checkout" value="Checkout" ${empty sessionItem ? 'disabled' : ''}>
                    Checkout
                </button>
            </form:form>
        </td>
        <td></td>
        <td>
            <form:form action="/shoppingCart/clear" method="post" modelAttribute="item">
                <button class="clear-button" type="submit" name="clear" id="clear" value="clear">
                    Clear Shopping Cart
                </button>
            </form:form>
        </td>
    </tr>
</table>

<%@ include file="../footer.jsp" %>