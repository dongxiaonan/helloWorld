<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" scope="request" value="Login"/>
<%@ include file="header.jsp" %>

    <c:choose>
        <c:when test="${not empty error}">
            <div id="loginError" class="page-action error">
                Your login attempt was not successful, try again.<br /> Caused :
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:when>
        <c:otherwise>
            <div id="TrailBlazers - Login" class="page-action">
                Login with Username and Password
            </div>
        </c:otherwise>
    </c:choose>

	<form name='f' action="<c:url value='j_spring_security_check' />" method="post">
        <div>
            <label>User</label>
            <div class="controls">
                <input type='text' name='j_username' placeholder="Username"></td>
            </div>
        </div>

        <div>
            <label>Password</label>
            <div class="controls">
                <input type="password" name="j_password" id="j_password" placeholder="Password">
            </div>
        </div>

        <div>
            <div class="controls">
                <button type="submit" name="submit">Sign in</button>
            </div>
        </div>

	</form>

<%@ include file="footer.jsp" %>