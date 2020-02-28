package com.internousdev.rosso.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.rosso.dao.CartInfoDAO;
import com.internousdev.rosso.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware {

	private Map<String, Object> session;
	private String user_id;
	private int totalPrice;

	private List<CartInfoDTO> cartList = new ArrayList<CartInfoDTO>();

	public String execute() throws SQLException {

		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String result = ERROR;

		//セッションが有効か確認し、未ログインユーザーかログインユーザーか判別
		if (session.get("logined") == null) {
			return "sessionTimeout";

		} else if (session.get("logined").equals(1)) {
			user_id = session.get("userId").toString();

		} else if (session.get("logined").equals(0)) {
			user_id = session.get("tmpUserId").toString();
		}

		//カート情報の有無を判別
		if (cartInfoDAO.cartCheck(user_id)) {
			cartList = cartInfoDAO.getCartInfo(user_id);
		}

		totalPrice = cartInfoDAO.getTotalPrice(user_id);
		result = SUCCESS;

		return result;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<CartInfoDTO> getCartList(){
		return cartList;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

}
