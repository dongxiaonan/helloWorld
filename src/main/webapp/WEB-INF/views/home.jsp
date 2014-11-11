<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Home"/>
<%@ include file="header.jsp" %>
<c:set var="addToCartSuccess" value="${lastItem.name} has been added to your shopping cart." scope="page"/>
<c:set var="addToCartFailure" value="${lastItem.name} is no longer available" scope="page"/>

<c:if test="${lastItem != null}">
    <div id="resultMessage" class="page-action ${error == null ? 'success' : 'error'}">${error == null ? addToCartSuccess : addToCartFailure }</div>
    <% session.setAttribute("lastItem",null); %>
    <% session.setAttribute("error",null); %>
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
            <td>&pound; <c:out value="${item.price}"/></td>
            <td><c:out value="${item.description}"/></td>
            <td><c:out value="${item.type}"/></td>
            <td><c:out value="${item.quantity}"/></td>
            <td>
                <security:authorize ifNotGranted="ROLE_ADMIN" >
                    <form:form action="/shoppingCart/addToCart" method="post" modelAttribute="item">
                        <form:hidden path="itemId" value="${item.itemId}"/>
                        <button class="addToCart-button" type="submit" name="addToCart" id="addToCart" value="Add To Cart" ${enableMultipleItemsPerCart == false ? 'disabled' : '' }>
                            Add To Cart
                        </button>
                    </form:form>
                </security:authorize>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="footer.jsp" %>