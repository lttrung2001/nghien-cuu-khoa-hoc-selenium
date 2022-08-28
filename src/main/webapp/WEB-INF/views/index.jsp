<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tìm model điện thoại</title>
<base href="${pageContext.servletContext.contextPath }/">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
	integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.2/css/all.min.css"
	integrity="sha512-1sCRPdkRXhBV2PBLUdRb4tMg1w2YPf37qatUFeS7zlBy7jJI8Lf4VHwWfZZfpXtYSLy85pkm9GaYVYMfw5BC1A=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<link
	href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900"
	rel="stylesheet">

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/style.css">
<style type="text/css">
.filter-title {
	font-weight: bold;
	font-size: 18px;
}
</style>
</head>
<body>
	<c:if test="${not empty message }">
		<div class="alert alert-danger" role="alert">${message }</div>
	</c:if>
	<c:if test="${not empty tgdd_message }">
		<div class="alert alert-danger" role="alert">TGDD message:
			${tgdd_message }</div>
	</c:if>
	<c:if test="${not empty dmx_message }">
		<div class="alert alert-danger" role="alert">DMX message:
			${dmx_message }</div>
	</c:if>
	<c:if test="${not empty fpt_message }">
		<div class="alert alert-danger" role="alert">FPTSHOP message:
			${fpt_message }</div>
	</c:if>

	<nav class="container navbar navbar-light bg-light">
		<form class="form-inline" action="search" method="post">
			<input name="key" class="form-control mr-sm-2" type="search"
				placeholder="Nhập cấu hình tìm kiếm" aria-label="Search">
			<button name="btnSearch" value="btnSearch" class="btn btn-outline-success my-2 my-sm-0" type="submit">Tìm kiếm</button>
		</form>
	</nav>

	<form:form cssClass="container d-flex flex-column bg-light"
		method="post" action="result" modelAttribute="phoneConfiguration">
		<div class="filter-title">HÃNG</div>
		<ul class="d-flex flex-wrap ks-cboxtags">
			<form:checkboxes element="li"
				cssClass="item form-check form-check-inline" items="${ft.brands }"
				path="brand" />
		</ul>
		<div class="filter-title">KHOẢNG GIÁ</div>
		<ul class="d-flex  flex-wrap ks-cboxtags">
			<form:checkboxes element="li"
				cssClass="item form-check form-check-inline"
				items="${ft.priceRanges }" path="priceRange" />
		</ul>
		<div class="filter-title">RAM</div>
		<ul class="d-flex  flex-wrap ks-cboxtags">
			<form:checkboxes element="li"
				cssClass="item form-check form-check-inline" items="${ft.ram }"
				path="ram" title="Ram" />
		</ul>
		<div class="filter-title">BỘ NHỚ TRONG</div>
		<ul class="d-flex  flex-wrap ks-cboxtags">
			<form:checkboxes element="li"
				cssClass="item form-check form-check-inline" items="${ft.rom }"
				path="rom" />
		</ul>
		<div class="filter-title">KÍCH THƯỚC MÀN HÌNH</div>
		<ul class="d-flex  flex-wrap ks-cboxtags">
			<form:checkboxes element="li"
				cssClass="item form-check form-check-inline"
				items="${ft.displaySize }" path="displaySize" />
		</ul>
		<div class="filter-title">TÍNH NĂNG</div>
		<ul class="d-flex  flex-wrap ks-cboxtags">
			<form:checkboxes element="li"
				cssClass="item form-check form-check-inline"
				items="${ft.specialFeatures }" path="specialFeature" />
		</ul>
		<form:button class="btn btn-primary">Tìm kiếm</form:button>
	</form:form>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/popper.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/main.js"></script>
</body>
</html>