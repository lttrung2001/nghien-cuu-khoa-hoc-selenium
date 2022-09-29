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
<link rel="stylesheet" href="/css/fancy-card.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"
	integrity="sha512-aVKKRRi/Q/YV+4mjoKBsE4x3H+BkegoM/em46NNlCqNTmUYADjBbeNefNxYV7giUp0VxICtqdrbqU7iVaeZNXA=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
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
	<label for="sort-type">Sắp xếp theo: </label>
	<select id="sort-type">
		<option value=""></option>
		<option value="asc">Giá tăng dần</option>
		<option value="dsc">Giá giảm dần</option>
	</select>
	<button class="btn btn-primary" onclick="history.back()">Quay
		lại trang tìm kiếm cấu hình</button>
	<div id="phone-container"
		style="display: flex; flex-wrap: wrap; justify-content: center;">
		<c:forEach var="item" items="${resultList }">
			<div class="card fancy_card phone-item"
				style="width: 18rem; display: inline-block;">
				<img class="card-img-top" src="${item.imageLink }"
					alt="Card image cap">
				<div class="card-body">
					<b class="card-title">${item.name }</b>
					<p style="font-weight: 500;" class="card-text current-price">
						Giá: <span>${item.price }</span>
					</p>
					<c:forEach var="p" items="${item.details }">
						<p class="card-text">${p }</p>
					</c:forEach>
					<a target="_blank" href="${item.productLink }"
						class="btn btn-primary">Link sản phẩm</a>
				</div>
			</div>
		</c:forEach>
	</div>
	<script type="text/javascript">
		$('#sort-type')
				.change(
						function() {
							const sortValue = $(this).val();
							var list = document
									.getElementById('phone-container');
							var nodesToSort = list
									.querySelectorAll('.phone-item');
							if (sortValue === 'asc') {
								Array.prototype.map
										.call(
												nodesToSort,
												function(node) {
													var price = parseFloat(node
															.querySelector('.current-price > span').textContent
															.replaceAll(".", "")
															.replace("₫", ""))
													price = isNaN(price) ? 0.0
															: price;
													console.log(price);
													return {
														node : node,
														relevantText : price
													};
												}).sort(
												function(a, b) {
													return a.relevantText
															- b.relevantText;
												}).forEach(function(item) {
											list.appendChild(item.node);
										});
							} else if (sortValue === 'dsc') {
								Array.prototype.map
										.call(
												nodesToSort,
												function(node) {
													var price = parseFloat(node
															.querySelector('.current-price > span').textContent
															.replaceAll(".", "")
															.replace("₫", ""))
													price = isNaN(price) ? 0.0
															: price;
													return {
														node : node,
														relevantText : price
													};
												}).sort(
												function(a, b) {
													return b.relevantText
															- a.relevantText;
												}).forEach(function(item) {
											list.appendChild(item.node);
										});
							}
						})
	</script>
</body>
</html>