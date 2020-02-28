package com.internousdev.rosso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.rosso.dto.CartInfoDTO;
import com.internousdev.rosso.util.DBConnector;

public class CartInfoDAO {

	//ユーザーIDと仮ユーザーIDに紐づくカートの中身を確認
	public boolean cartCheck(String userId) throws SQLException {

		boolean result = false;
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "SELECT product_id FROM cart_info WHERE user_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	//ユーザーIDと仮ユーザーIDに紐づく商品IDが存在する場合は商品情報を取得
	public List<CartInfoDTO> getCartInfo(String userId) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();

		String sql = "SELECT"
				+ " ci.id as id,"
				+ " ci.user_id as user_id,"
				+ " ci.product_id as product_id,"
				+ " ci.product_count as product_count,"
				+ " pi.price as price,"
				+ " pi.product_name as product_name,"
				+ " pi.product_name_kana as product_name_kana,"
				+ " pi.image_file_path as image_file_path,"
				+ " pi.image_file_name as image_file_name,"
				+ " pi.release_date as release_date,"
				+ " pi.release_company as release_company,"
				+ " pi.status as status,"
				+ " ci.regist_date as regist_date,"
				+ " ci.update_date as update_date "
				+ "FROM cart_info as ci "
				+ "LEFT JOIN product_info as pi "
				+ "ON ci.product_id = pi.product_id "
				+ "WHERE ci.user_id = ? "
				+ "ORDER BY update_date desc, regist_date desc";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				CartInfoDTO cartInfoDTO = new CartInfoDTO();
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
				cartInfoDTO.setStatus(rs.getInt("status"));
				cartInfoDTO.setRegistDate(rs.getString("regist_date"));
				cartInfoDTO.setUpdateDate(rs.getString("update_date"));
				cartInfoDTO.setProductTotalPrice(cartInfoDTO.getPrice() * cartInfoDTO.getProductCount());
				cartInfoDTOList.add(cartInfoDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cartInfoDTOList;
	}

	//カート合計金額を取得
	public int getTotalPrice(String userId) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int totalPrice = 0;

		String sql = "SELECT p.price, c.product_count "
				+ "FROM cart_info c "
				+ "INNER JOIN product_info p "
				+ "ON c.product_id = p.product_id "
				+ "WHERE c.user_id = ? ";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				CartInfoDTO cartInfoDTO = new CartInfoDTO();
				cartInfoDTO.setPrice(rs.getInt("price"));
				cartInfoDTO.setProductCount(rs.getInt("product_count"));

				cartInfoDTO.setProductTotalPrice(cartInfoDTO.getPrice() * cartInfoDTO.getProductCount());

				if (totalPrice == 0) {
					totalPrice = cartInfoDTO.getProductTotalPrice();
				} else {
					totalPrice = totalPrice + cartInfoDTO.getProductTotalPrice();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}

	//チェックされた商品情報があれば、その商品idとその人のユーザー/仮ユーザーIDと一致するものを削除
	public int deleteCartInfo(String userId, String productId) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 1;

		String sql = "DELETE FROM cart_info WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, productId);
			count = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	//ユーザーIDに紐づくユーザー情報を削除
	public int SettlementDelete(String userId) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int rs = 0;

		String sql = "DELETE FROM cart_info WHERE user_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeUpdate();

			if (rs == 0) {
				rs = 0;
			} else {
				rs = 1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	//追加する商品IDと一致するデータが存在するか否か判別
	public boolean isCartInfoCheckId(String userId, int productId) throws SQLException {

		boolean result = false;
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "SELECT COUNT(id) AS COUNT FROM cart_info WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getInt("COUNT") > 0) {
					result = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	//追加する商品IDと一致するデータが存在する場合、個数を足した値で更新
	public int cartInfoUpdateAdd(String userId, int productId, int productCount) throws SQLException {


		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int cart = 0;

		String sql = "UPDATE cart_info SET product_count = product_count + ?,update_date=now() WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);
			cart = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cart;
	}

	//追加する商品IDと一致するデータが存在しない場合、カート情報を登録
	public int cartInfoInsert(String userId, int productId, int productCount) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int cart = 0;

		String sql = "INSERT INTO cart_info (user_id, product_id, product_count, regist_date, update_date)"
				+ "VALUES (?, ?, ?, now(), now())";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);
			cart = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cart;
	}

	//カート情報をアップデート
	public int cartInfoUpdateUserId(String userId, String tmpUserId, int productId) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int rs = 0;

		String sql = "UPDATE cart_info SET user_id = ?,update_date=now() WHERE user_id = ? AND product_id = ? ";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2,tmpUserId);
			ps.setInt(3, productId);
			rs = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

}
