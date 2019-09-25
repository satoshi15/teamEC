package com.internousdev.maple.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.maple.dao.CartInfoDAO;
import com.internousdev.maple.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction extends ActionSupport implements SessionAware {

	private Map<String, Object> session;
	private ArrayList<CartInfoDTO> cartInfoDTOList;
	private int productId;
	private int productCount;
	private int cartPrice;

	public String execute(){

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")){
			return "sessionTimeout";
		}

		String result =ERROR;
		String userId =null;

		int logined =Integer.parseInt(session.get("logined").toString());

		if(logined == 1){
			userId =session.get("userId").toString();
		}else{
			userId =session.get("tempUserId").toString();
		}


		if(productCount >= 1 && productCount<= 5){
			//  ユーザが同じ商品をカートに入れているか確認
			int count =0;
			CartInfoDAO cartInfoDAO =new CartInfoDAO();

			if(cartInfoDAO.isExistsCartInfo(userId, productId)){
				//  カートにある場合は個数を変更
				count =cartInfoDAO.addCart(productCount, userId, productId);
			}else{
				//  カートにない場合は新規登録
				count =cartInfoDAO.register(userId, productId, productCount);
			}

			if(count > 0){
				cartInfoDTOList =cartInfoDAO.getCartInfo(userId);
				cartPrice =cartInfoDAO.getCartPrice(userId);

				result =SUCCESS;
			}
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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public int getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(int cartPrice) {
		this.cartPrice = cartPrice;
	}
}
