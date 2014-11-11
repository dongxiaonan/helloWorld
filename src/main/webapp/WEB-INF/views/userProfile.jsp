<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="User Profile"/>
<%@ include file="header.jsp" %>

<security:authorize ifAnyGranted="ROLE_ADMIN">
    <c:set var="titlePrefix" value="${userDetail.account_name}'s"/>
</security:authorize>

<security:authorize ifAnyGranted="ROLE_USER">
    <c:set var="titlePrefix" value="Your"/>
</security:authorize>

<div class="page-action">${titlePrefix} details</div>
        <div id="user-details" >
            <span class="user-name">${userDetail.account_name}</span> - <span class="email-address">${userDetail.email_address}</span><br />
            <span class="user-address-title">Address:</span><br/>
            <span class="user-address">${address} </span><br />
            <span class="phone-number">Phone Number : ${userDetail.phoneNumber}</span>
        </div>

		<div class="page-action">${titlePrefix} Orders</div>
		<table id= "order-table" class="table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Description</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
            <c:set var="index" scope="session" value="${0}"/>
            <c:forEach var="item" items="${items}" varStatus="row">
                <tr id=row<c:out value="${index}"/>>
                    <td><c:out value="${item.name}"/></td>
                    <td>&#163;&nbsp;<c:out value="${item.price}"/></td>
                    <td><c:out value="${item.description}"/></td>
                    <td><c:out value="${item.type}"/></td>
                    <c:set var="index" scope="session" value="${index+1}"/>
                </tr>
             </c:forEach>
            </tbody>
        </table>

<%@ include file="footer.jsp" %>