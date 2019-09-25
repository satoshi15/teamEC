package com.internousdev.maple.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.internousdev.maple.dto.CartInfoDTO;
import com.internousdev.maple.util.DBConnector;

public class CartInfoDAO {
	//カート情報を表示
	public ArrayList<CartInfoDTO> getCartInfo(String userId){
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();
		ArrayList<CartInfoDTO> cartInfoDTOList =new ArrayList<>();

		String sql ="SELECT "
				+ "ci.id as id, "
				+ "ci.user_id as user_id, "
				+ "ci.product_id as product_id, "
				+ "ci.product_count as product_count, "
				+ "pi.price as price, "
				+ "pi.product_name as product_name, "
				+ "pi.product_name_kana as product_name_kana, "
				+ "pi.image_file_path as image_file_path, "
				+ "pi.image_file_name as image_file_name, "
				+ "pi.release_date as release_date, "
				+ "pi.release_company as release_company, "
				+ "(ci.product_count * pi.price) as item_total_price, "
				+ "ci.regist_date as regist_date, "
				+ "ci.update_date as update_date "
				+ "FROM cart_info as ci "
				+ "LEFT JOIN product_info as pi "
				+ "ON ci.product_id = pi.product_id "
				+ "WHERE ci.user_id = ? "
				+ "ORDER BY update_date DESC, regist_date DESC ";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs =ps.executeQuery();

			while(rs.next()){
				CartInfoDTO cartInfoDTO =new CartInfoDTO();
				cartInfoDTO.setId(rs.getInt("id"));
				cartInfoDTO.setUserId(rs.getString("user_id"));
				cartInfoDTO.setProductId(rs.getInt("product_id"));
				cartInfoDTO.setProductCount(rs.getInt("product_count"));
				cartInfoDTO.setPrice(rs.getInt("price"));
				cartInfoDTO.setProductName(rs.getString("product_name"));
				cartInfoDTO.setProductNameKana(rs.getString("product_name_kana"));
				cartInfoDTO.setImageFilePath(rs.getString("image_file_path"));
				cartInfoDTO.setImageFileName(rs.getString("image_file_name"));
				cartInfoDTO.setReleaseDate(rs.getDate("release_date"));
				cartInfoDTO.setReleaseCompany(rs.getString("release_company"));
				cartInfoDTO.setItemTotalPrice(rs.getInt("item_total_price"));

				cartInfoDTOList.add(cartInfoDTO);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return cartInfoDTOList;
	}

	//カート全体の合計金額を表示
	public int getCartPrice(String userId){
		int cartPrice =0;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql ="SELECT "
				+ "sum(product_count * price) as cart_price "
				+ "FROM cart_info as ci "
				+ "JOIN product_info as pi "
				+ "ON ci.product_id = pi.product_id "
				+ "WHERE user_id =? "
				+ "GROUP BY user_id ";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs =ps.executeQuery();

			if(rs.next()){
				cartPrice =rs.getInt("cart_price");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return cartPrice;
	}

	/*カートに追加時、同じ商品がカートに追加されていればtrue
  追加されていなければfalse を返す*/
	public boolean isExistsCartInfo(String userId, int productId){
		boolean result =false;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql="SELECT "
				+ "count(id) as count "
				+ "FROM cart_info "
				+ "WHERE user_id =? AND product_id =? ";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);

			ResultSet rs =ps.executeQuery();

			if(rs.next()){
				if(rs.getInt("count") > 0){
					result =true;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return result;
	}

	//同じ商品をカートに追加する場合、商品購入個数を変更する
	public int addCart(int productCount, String userId, int productId){
		int count =0;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql ="UPDATE "
				+ "cart_info "
				+ "SET product_count =(product_count + ?), update_date = now() "
				+ "WHERE user_id =? AND product_id =? ";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);

			count =ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	//新規商品をカートDBに追加
	public int register(String userId, int productId, int productCount){
		int count =0;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql ="INSERT "
				+ "INTO cart_info (user_id, product_id, product_count, "
				+ "regist_date, update_date) "
				+ "VALUES(?, ?, ?, now(), now() )";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);

			count =ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	//カート上の商品を削除する
	public int delete(String userId, String productId){
		int count =0;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql="DELETE "
				+ "FROM cart_info "
				+ "WHERE user_id =? AND product_id =? ";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, productId);

			count =ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	//仮ユーザーIDのカート情報をユーザーIDに紐付ける
	public int connectUserId(String userId, String tempUserId, int productId){
		int count =0;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql="UPDATE cart_info "
				+ "SET user_id =?, update_date = now() "
				+ "WHERE user_id =? AND product_id =?";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);

			count =ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	//決済完了時にカート情報を削除する
	public int deleteAll(String userId){
		int count =0;
		DBConnector db =new DBConnector();
		Connection con =db.getConnection();

		String sql="DELETE "
				+ "FROM cart_info "
				+ "WHERE user_id =? ";

		try{
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setString(1, userId);

			count =ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}
}
