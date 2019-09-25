package com.internousdev.maple.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.maple.dao.CartInfoDAO;
import com.internousdev.maple.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware{

	private Map<String, Object> session;
	private ArrayList<CartInfoDTO> cartInfoDTOList;
	private int cartPrice;

	public String execute(){

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")){
			return "sessionTimeout";
		}

		String userId =null;

		int logined =Integer.parseInt(session.get("logined").toString());
		CartInfoDAO cartInfoDAO =new CartInfoDAO();

		if(logined == 1){
			userId =session.get("userId").toString();
		}else{
			userId =session.get("tempUserId").toString();
		}

		cartInfoDTOList =cartInfoDAO.getCartInfo(userId);
		cartPrice =cartInfoDAO.getCartPrice(userId);

		return SUCCESS;
	}

	public Map<String, Object> getSession(){
		return this.session;
	}
	public void setSession(Map<String, Object> session){
		this.session =session;
	}

	public ArrayList<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}

	public void setCartInfoDTOList(ArrayList<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}

	public int getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(int cartPrice) {
		this.cartPrice = cartPrice;
	}
}