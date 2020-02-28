package com.internousdev.rosso.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.rosso.dao.CartInfoDAO;
import com.internousdev.rosso.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware {

	private String[] checkList;
	List<CartInfoDTO> cartList = new ArrayList<CartInfoDTO>();

	private Map<String, Object> session;
	private String userId;
	private boolean cartCheck;
	private int totalPrice;

	public String execute() throws SQLException {

		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String result = ERROR;

		int count = 0;

		//セッションが有効か確認し、未ログインユーザーかログインユーザーか判別
		if (session.get("logined") == null) {
			return "sessionTimeout";

		} else if (session.get("logined").equals(1)) {
			userId = session.get("userId").toString();

		} else if (session.get("logined").equals(0)) {
			userId = session.get("tmpUserId").toString();
		}

		//チェックBOXにチェックがあるものを削除
		for(String productId:checkList) {
			count += cartInfoDAO.deleteCartInfo(userId, productId);
		}

		if(count == checkList.length) {
			cartList = cartInfoDAO.getCartInfo(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);

			result = SUCCESS;
		}

		return result;
	}

	public String[] getCheckList() {
		return checkList;
	}

	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
	}

	public List<CartInfoDTO> getCartList() {
		return cartList;
	}

	public void setCartList(List<CartInfoDTO> cartList) {
		this.cartList = cartList;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getUser_id() {
		return userId;
	}

	public void setUser_id(String user_id) {
		this.userId = user_id;
	}

	public boolean isCartCheck() {
		return cartCheck;
	}

	public void setCartCheck(boolean cartCheck) {
		this.cartCheck = cartCheck;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
