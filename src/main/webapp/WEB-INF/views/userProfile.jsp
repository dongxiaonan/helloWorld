<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="User Profile"/>
<%@ include file="header.jsp" %>

<div class="page-action">Your details</div>
        <div id="user-details" >
            <span class="user-name">${userDetail.account_name}</span> - <span class="email-address">${userDetail.email_address}</span><br />
            <span class="street1">Street1 : ${userDetail.street1}</span><br />
            <span class="street2">Street2 : ${userDetail.street2}</span><br />
            <span class="city">City : ${userDetail.city}</span><br />
            <span class="state-province">State/Province : ${userDetail.state_Province}</span><br />
            <span class="country">Country : ${userDetail.country.name}</span><br />
            <span class="post-code">Post Code : ${userDetail.postcode}</span>
        </div>

		<div class="page-action">Your Orders</div>
		<table class="table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Description</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${items}" varStatus="row">
                <tr>
                    <td><c:out value="${item.name}"/></td>
                    <td><c:out value="${item.price}"/></td>
                    <td><c:out value="${item.description}"/></td>
                    <td><c:out value="${item.type}"/></td>
                </tr>
             </c:forEach>
            </tbody>
        </table>

<%@ include file="footer.jsp" %>