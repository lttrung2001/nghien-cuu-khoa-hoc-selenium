<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tìm model điện thoại</title>
<base href="${pageContext.servletContext.contextPath }/">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
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
		<div class="alert alert-danger" role="alert">TGDD message: ${tgdd_message }</div>
	</c:if>
	<c:if test="${not empty dmx_message }">
		<div class="alert alert-danger" role="alert">DMX message: ${dmx_message }</div>
	</c:if>
	<c:if test="${not empty fpt_message }">
		<div class="alert alert-danger" role="alert">FPTSHOP message: ${fpt_message }</div>
	</c:if>
	<form:form cssClass="container d-flex flex-column" method="post"
		action="result" modelAttribute="phoneConfiguration">
		<div class="filter-title">HÃNG</div>
		<div class="d-flex justify-content-around flex-wrap">
			<form:checkboxes cssStyle="transform: scale(2);" cssClass="form-check form-check-inline"
				items="${ft.brands }" path="brand" />
		</div>
		<br>
		<br>
		<div class="filter-title">KHOẢNG GIÁ</div>
		<div class="d-flex justify-content-around flex-wrap">
			<form:checkboxes cssStyle="transform: scale(2);" cssClass="form-check form-check-inline"
				items="${ft.priceRanges }" path="priceRange" />
		</div>
		<br>
		<br>
		<div class="filter-title">RAM</div>
		<div class="d-flex justify-content-around flex-wrap">
			<form:checkboxes cssStyle="transform: scale(2);" cssClass="form-check form-check-inline"
				items="${ft.ram }" path="ram" title="Ram" />
		</div>
		<br>
		<br>
		<div class="filter-title">BỘ NHỚ TRONG</div>
		<div class="d-flex justify-content-around flex-wrap">
			<form:checkboxes cssStyle="transform: scale(2);" cssClass="form-check form-check-inline"
				items="${ft.rom }" path="rom" />
		</div>
		<br>
		<br>
		<div class="filter-title">KÍCH THƯỚC MÀN HÌNH</div>
		<div class="d-flex justify-content-around flex-wrap">
			<form:checkboxes cssStyle="transform: scale(2);" cssClass="form-check form-check-inline"
				items="${ft.displaySize }" path="displaySize" />
		</div>
		<br>
		<br>
		<div class="filter-title">TÍNH NĂNG</div>
		<div class="d-flex justify-content-around flex-wrap">
			<form:checkboxes cssStyle="transform: scale(2);" cssClass="form-check form-check-inline"
				items="${ft.specialFeatures }" path="specialFeature" />
		</div>
		<br>
		<form:button class="btn btn-primary">Tìm kiếm</form:button>
	</form:form>
</body>
</html>