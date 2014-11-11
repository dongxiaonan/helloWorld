<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<c:set var="enableMultipleItemsPerCart" scope="application" value="true"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="<c:url value='/scripts/css/main.css' />" type="text/css"/>
    <script type="text/javascript" src="<c:url value='/scripts/lib/jquery-1.10.2.js' />"></script>
    <script type="text/javascript" src="<c:url value='/scripts/lib/jquery.cookie.js' />"></script>
    <script type="text/javascript" src="/scripts/js/Survey.js" ></script>
</head>
<body>

<div class="navbar">
    <a class="brand" href="<c:url value='/' />">
        <img width="20px;" src="<c:url value='/images/logo.png' />">
        Freewheelers
    </a>
    <ul class="nav">
        <li><a href="<c:url value='/' />" class="header-link">Home</a></li>

        <security:authorize ifNotGranted="ROLE_ADMIN,ROLE_USER">
            <li><a href="<c:url value='/login' />" class="header-link">Login</a></li>
        </security:authorize>

        <security:authorize ifAnyGranted="ROLE_USER">
            <li><a href="<c:url value="/shoppingCart/myShoppingCart" />" class="header-link ${enableMultipleItemsPerCart == false ? 'disableClick' : ''}">My Shopping Cart</a></li>
            <li><a href="<c:url value='/userProfile' />" class="header-link">User Profile</a></li>
        </security:authorize>

        <security:authorize ifAnyGranted="ROLE_ADMIN">
            <li><a href="<c:url value="/survey/report" />" class="header-link">NPS Report</a></li>
            <li><a href="<c:url value='/admin' />" class="header-link">Admin Profile</a></li>
        </security:authorize>

        <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
            <li><a href="<c:url value="j_spring_security_logout" />" class="header-link">Logout</a></li>
        </security:authorize>

        <li><a href="<c:url value='/account/create' />" class="header-link">Create Account</a></li>

        <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
            <li id="welcome" class="navbar-text">Welcome <security:authentication property="principal.username"/>!</li>
        </security:authorize>
    </ul>

</div>

<!--[if lt IE 9]>
<div class="alert">
    You are using a Legacy Browser - it is not supported. Please upgrade to <a
        href="http://windows.microsoft.com/en-US/internet-explorer/downloads/ie-9/worldwide-languages">IE9</a>, Firefox,
    Safari, Chrome or Opera.
</div>
<![endif]-->
