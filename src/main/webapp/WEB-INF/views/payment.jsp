<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Payment"/>
<%@ include file="header.jsp" %>


<div class="page-action" id="shippingAddress">
    Shipping Address
</div>

<c:if test="${not empty address}">
    ${address}
    <br/>
    <br/>
    <span>*Note: Shipping and billing addresses are the same</span>
</c:if>

<div class="page-action">Payment Details</div>

<c:if test="${not empty paymentErrorMessage}">
    <div id="paymentErrorMessage" class="page-action error"> ${paymentErrorMessage} </div>
</c:if>

<form action="/cardPayment/submitPayment" method="post">
    <div>
        <label for="fld_cardHolderName">Card Holder Name</label>
        <div class="controls">
            <input type="text" id="fld_cardHolderName" placeholder="John Smith" name="cardHolderName" value="">
        </div>
    </div>

    <div>
        <label for="fld_cardNumber">Card Number<span>*</span></label>
        <div class="controls">
            <input type="text" id="fld_cardNumber" placeholder="1000010001000100" name="cardNumber"
                   value="${not empty errors['cardNumber']==true? '':param.cardNumber}"/>
        </div>
        <c:if test="${not empty errors['cardNumber']}">
            <span class="text-error"> ${errors['cardNumber']}</span>
        </c:if>
    </div>
    <div>
        <label for="sel_expiration_date_month">Expiry Date<span>*</span></label>
        <div class="controls">
            <select id="sel_expiration_date_month" name="expiryDateMonth" style="width: 116px">
                <option value="">Month</option>
                <c:forEach var="month" items="${months}">
                    <option ${param.expiryDateMonth == month? 'selected': ''} value="${month}">${month}</option>
                </c:forEach>
            </select>
            <select id="sel_expiration_date_year" name="expiryDateYear" style="width: 116px">
                <option value="">Year</option>
                <c:forEach var="year" items="${years}">
                    <option ${param.expiryDateYear == year? 'selected': ''} value="${year}">${year}</option>
                </c:forEach>
            </select>

            <c:if test="${not empty errors['expirationDate']}">
                <span class="text-error"> ${errors['expirationDate']}</span>
            </c:if>
        </div>
    </div>
    <div>
        <label for="fld_csc">CCV<span>*</span></label>
        <div class="controls">
            <input type="text" id="fld_csc" placeholder="1234" name="CCV"
                   value="${not empty errors['CCV']==true? '':param.CCV}"/>
            <span onmouseover="mOver(this)" onmouseout="mOut(this)"> ? </span>
            <c:if test="${not empty errors['CCV']}">
                <span class="text-error"> ${errors['CCV']}</span>
            </c:if>
        </div>

    </div>
    <div>
        <label id="label_amount">Amount: </label>
        <div class="controls">
            <label type="text" id="fld_amount" name="amount">
                <c:if test="${not empty sessionItems}">&pound; ${totalCartPrice}</c:if>
            </label>
            <input type="hidden" name="price" value="${totalCartPrice}">
        </div>
    </div>
    <div style="text-align: right;">
        <button class="submitPayment-button" type="submit" id="submitPayment" value="submit">
            Buy Now
        </button>
    </div>
</form>

<script>
    function mOver(obj) {
        obj.innerHTML = "The last three digits in the back of your card"
    }

    function mOut(obj) {
        obj.innerHTML = "?"
    }
</script>

<%@ include file="footer.jsp" %>