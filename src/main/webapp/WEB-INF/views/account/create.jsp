<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" scope="request" value="Create Account"/>

<%@ include file="../header.jsp" %>

    <div class="page-action">
        Create a new account
    </div>

    <c:if test="${not empty validationMessage.errors}">
        <div id="resultsMessage" class="page-action error">
            There were errors.
        </div>
    </c:if>

	<form action="/account/create" method="post">
            <label for="fld_email">Email</label>
            <div class="controls">
                <input type="text" id="fld_email" placeholder="somebody@something.com" name="email" value="${account.email_address}">
                <c:if test="${not empty validationMessage.errors['email']}">
                    <span class="text-error">${validationMessage.errors["email"]}</span>
                </c:if>
        </div>

        <div>
            <label for="fld_password">Password</label>
            <div class="controls">
                <input type="password" id="fld_password" placeholder="secret password" name="password" value="${account.password}">
                <c:if test="${not empty validationMessage.errors['password']}">
                    <span class="text-error">${validationMessage.errors["password"]}</span>
                </c:if>
            </div>
        </div>

        <div>
            <label for="fld_name">Name</label>
            <div class="controls">
                <input type="text" id="fld_name" placeholder="Your Name" name="name" value="${account.account_name}">
                <c:if test="${not empty validationMessage.errors['name']}">
                    <span class="text-error">${validationMessage.errors["name"]}</span>
                </c:if>
            </div>
        </div>

        <div>
            <label for="sel_country">Country</label>
            <div class="controls">
                <select id="sel_country" name="country" >
                    <option value="">Select</option>
                    <c:forEach var="country" items="${countries}">
                        <option <c:if test="${(account.country == country)}" >selected </c:if>value="${country}">${country}</option>
                    </c:forEach>
                </select>
                <c:if test="${not empty validationMessage.errors['country']}">
                    <span class="text-error">${validationMessage.errors["country"]}</span>
                </c:if>
            </div>
        </div>

        <div>
            <label for="fld_phoneNumber">Phone Number</label>
            <div class="controls">
                <input type="text" id="fld_phoneNumber" placeholder="555-123456" name="phoneNumber" value="${account.phoneNumber}">
                <c:if test="${not empty validationMessage.errors['phoneNumber']}">
                    <span class="text-error">${validationMessage.errors["phoneNumber"]}</span>
                </c:if>
            </div>
        </div>

        <div>
            <div class="controls">
                <button type="submit" id="createAccount" value="Submit">Create Account</button>
            </div>
        </div>

	</form>

    <div class="note">
        If your country is not listed then we don't ship there. Please check back later.
    </div>

<%@ include file="../footer.jsp" %>
