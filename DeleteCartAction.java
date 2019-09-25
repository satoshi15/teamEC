package com.internousdev.maple.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.maple.dao.CartInfoDAO;
import com.internousdev.maple.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware {

	private Map<String, Object> session;
	private ArrayList<CartInfoDTO> cartInfoDTOList;
	private int cartPrice;
	private String[] checkList;

	public String execute(){

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")){
			return "sessionTimeout";
		}

		String result =ERROR;
		String userId =null;
		int count =0;
		CartInfoDAO cartInfoDAO =new CartInfoDAO();

		int logined =Integer.parseInt(session.get("logined").toString());

		if(logined == 1){
			userId =session.get("userId").toString();
		}else{
			userId =session.get("tempUserId").toString();
		}

		for(String productId : checkList){
			count += cartInfoDAO.delete(userId, productId);
		}

		if(count == checkList.length){
			cartInfoDTOList =cartInfoDAO.getCartInfo(userId);
			cartPrice =cartInfoDAO.getCartPrice(userId);

			result =SUCCESS;
		}
		return result;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
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

	public String[] getCheckList() {
		return checkList;
	}

	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
	}
}