<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Home"/>
<%@ include file="header.jsp" %>

<c:if test="${ lastItem != null }">
    <div id="resultMessage" class="page-action success"> ${lastItem.name} has been added to your shopping cart.</div>
    <% session.setAttribute("lastItem",null); %>
</c:if>
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Price</th>
        <th>Description</th>
        <th>Type</th>
        <th>Quantity</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${items}" varStatus="row">
        <tr>
            <td><c:out value="${item.name}"/></td>
            <td><c:out value="${item.price}"/></td>
            <td><c:out value="${item.description}"/></td>
            <td><c:out value="${item.type}"/></td>
            <td><c:out value="${item.quantity}"/></td>
            <td>
                <form:form action="/shoppingCart/myShoppingCart" method="post" modelAttribute="item">
                    <form:hidden path="itemId" value="${item.itemId}"/>
                    <button class="addToCart-button" type="submit" name="addToCart" id="addToCart" value="Add To Cart" ${enableMultipleItemsPerCart == false ? 'disabled' : '' }>
                        Add To Cart
                    </button>
                </form:form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="footer.jsp" %>