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
        <input type="text" id="fld_email" placeholder="somebody@something.com" name="email"
               value="${account.email_address}">
        <c:if test="${not empty validationMessage.errors['email']}">
            <span class="text-error">${validationMessage.errors["email"]}</span>
        </c:if>
    </div>


    <div>
        <text>Password needs to be between 8 and 20 characters, and contain at least 1 number, 1 lowercase letter, 1
            uppercase letter, and 1 special character.
        </text>
    </div>
    <div>
        <label for="fld_password">Password</label>

        <div class="controls">
            <input type="password" id="fld_password" placeholder="secret password" name="password"
                   value="${account.password}">
            <c:if test="${not empty validationMessage.errors['password']}">
                <span class="text-error">${validationMessage.errors["password"]}</span>
            </c:if>
        </div>
    </div>

    <div>
        <label for="fld_confirmedPassword">Confirm Password</label>
        <div class="controls">
            <input type="password" id="fld_confirmedPassword" placeholder="secret password" name="confirmedPassword"
                   value="${confirmedPassword}">
            <c:if test="${not empty validationMessage.errors['confirmedPassword']}">
                <span class="text-error">${validationMessage.errors["confirmedPassword"]}</span>
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
            <label for="fld_street1">Street1</label>
            <div class="controls">
                <input type="text" id="fld_street1" placeholder="Your Street Address" name="street1" value="${account.street1}">
                <c:if test="${not empty validationMessage.errors['street1']}">
                    <span class="text-error">${validationMessage.errors["street1"]}</span>
                </c:if>
            </div>
        </div>

        <div>
            <label for="fld_street2">Street2</label>
            <div class="controls">
                <input type="text" id="fld_street2" placeholder="Continuation of Your Street Address" name="street2" value="${account.street2}">
            </div>
        </div>

        <div>
            <label for="fld_city">City</label>
            <div class="controls">
                <input type="text" id="fld_city" placeholder="Your City" name="city" value="${account.city}">
                <c:if test="${not empty validationMessage.errors['city']}">
                    <span class="text-error">${validationMessage.errors["city"]}</span>
                </c:if>
            </div>
        </div>

        <div>
            <label for="fld_stateProvince">StateProvince</label>
            <div class="controls">
                <input type="text" id="fld_stateProvince" placeholder="Your State/Province" name="stateProvince" value="${account.state_Province}">
            </div>
        </div>

        <div>
            <label for="sel_country">Country</label>
            <div class="controls">
                <select id="sel_country" name="country" >
                    <option value="">Select</option>
                    <c:forEach var="country" items="${countries}">
                        <option ${account.country == country ? 'selected' : ''} value="${country.name}">${country.name}</option>
                    </c:forEach>
                </select>
                <c:if test="${not empty validationMessage.errors['country']}">
                    <span class="text-error">${validationMessage.errors["country"]}</span>
                </c:if>
            </div>

        </div>
    </div>

        <div>
            <label for="fld_postcode">Postcode</label>
            <div class="controls">
                <input type="text" id="fld_postcode" placeholder="Your Post Code" name="postcode" value="${account.postcode}">
                <c:if test="${not empty validationMessage.errors['postcode']}">
                    <span class="text-error">${validationMessage.errors["postcode"]}</span>
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
