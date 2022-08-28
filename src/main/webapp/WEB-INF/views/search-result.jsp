<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kết quả tìm kiếm</title>
<base href="${pageContext.servletContext.contextPath }/">
<style type="text/css">
span, h1 {
	font-weight: 700;
}
</style>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
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
	<h1>Cấu hình tìm kiếm</h1>
	<a class="btn btn-primary" href="/">Quay lại trang tìm kiếm
		cấu hình</a>
	<div style="display: flex; flex-wrap: wrap; justify-content: center;">
		<c:forEach var="item" items="${resultList }">
			<div class="card"
				style="width: 18rem; display: inline-block;">
				<img class="card-img-top" src="${item.imageLink }"
					alt="Card image cap">
				<div class="card-body">
					<h5 class="card-title">${item.name }</h5>
					<p style="font-weight: 500;" class="card-text">Giá:
						${item.price }</p>
					<c:forEach var="p" items="${item.details }">
						<p class="card-text">${p }</p>
					</c:forEach>
					<a target="_blank" href="${item.productLink }"
						class="btn btn-primary">Link sản phẩm</a>
				</div>
			</div>
		</c:forEach>
	</div>
</body>
</html>