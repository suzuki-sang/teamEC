<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="./css/table.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<title>カート画面</title>
<script type="text/javascript">
	//chackValue関数(function)
	function checkValue() {
		var checkList = document.getElementsByClassName("checkList");
		var checkFlag = 0;
		for (var i = 0; i < checkList.length; i++) {
			//チェックされていた時
			if (checkList[i].checked) {
				checkFlag = 1;
				break;
			}
		}
		//チェックがある場合
		if (checkFlag == 1) {
			//削除ボタン有効
			document.getElementById('deleteButton').disabled = "";
		} else {
			//削除ボタン無効
			document.getElementById('deleteButton').disabled = "true";
		}
	}
</script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div id="main">
		<div id="top">
			<h1>カート画面</h1>
		</div>
		<s:if test="%{cartList.size() == 0}">
			<div class="info">カート情報がありません。</div>
		</s:if>
		<s:if test="%{cartList != null && cartList.size() > 0}">
			<s:form action="DeleteCartAction">
				<table class="cart_table">
					<tr>
						<th><label>#</label></th>
						<th><label>商品名</label></th>
						<th><label>商品名ふりがな</label></th>
						<th><label>商品画像</label></th>
						<th><label>値段</label></th>
						<th><label>発売会社名</label></th>
						<th><label>発売年月日</label></th>
						<th><label>購入個数</label></th>
						<th><label>合計金額</label></th>
					</tr>
					<s:iterator value="cartList">
						<tr>
							<td><input type="checkbox" class="checkList"
								name="checkList" value="<s:property value = 'productId'/>"
								onchange="checkValue()"></td>
							<td><s:property value="productName" /></td>
							<td><s:property value="productNameKana" /></td>
							<td><img
								src='<s:property value="imageFilePath"/>/<s:property value="imageFileName"/>'
								width="50px" height="50px" /></td>
							<td><s:property value="price" /><span>円</span></td>
							<td><s:property value="releaseCompany" /></td>
							<td><s:property value="releaseDate" /></td>
							<td><s:property value="productCount" /></td>
							<td><s:property value="productTotalPrice" /><span>円</span></td>
						</tr>
					</s:iterator>
				</table>
				<h3>
					カート合計金額：
					<s:property value="totalPrice" />
					<span>円</span>
				</h3>
				<div class="submit_btn_cart">
					<s:submit value="削除" id="deleteButton" class="submit_btn"
						disabled="true" />
				</div>
			</s:form>
			<s:if test="#session.logined == 1">
				<s:form action="SettlementConfirmAction">
					<div class="submit_btn_box_cart">
						<s:submit value="決済" id="settlementButton" class="submit_btn" />
					</div>
				</s:form>
			</s:if>
			<s:if test="#session.logined == 0  ||  #session.logined == null">
				<s:form action="GoLoginAction">
					<div class="submit_btn_box_cart">
						<s:hidden name="cartFlg" value="1" />
						<s:submit value="決済" id="settlementButton" class="submit_btn" />
					</div>
				</s:form>
			</s:if>
		</s:if>
	</div>
</body>
</html>