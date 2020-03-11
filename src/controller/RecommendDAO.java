package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ProductVO;
import model.RecommendVO;

public class RecommendDAO {
	// ����ȭ�鿡�� ��õ ��ư�� ������ ����Ǵ� �޼ҵ�
	public void getRecommendRegiste(String id, String productName) throws Exception {
		String dml = "insert into recommendtbl " + "(id, productName) " + "values " + "(?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// DBUtil Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� ��ǰ ������ ó���ϱ� ���Ͽ� SQL������ ����
			pstmt = con.prepareStatement(dml); // =������ �غ��ϰڴ�. dml�� �־������ ?�� 1���� �ȴ�.

			pstmt.setString(1, id);
			pstmt.setString(2, productName);
			// SQL���� ������ ó�� ����� ����
			pstmt.execute();// ����� �����ߴ���

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return;
	}

	// ����ȭ�鿡 �ִ� TOP3 ����
	public ArrayList<RecommendVO> getRecommendResult() {
		ArrayList<RecommendVO> list = new ArrayList<RecommendVO>();
		String dml = "select B.productName, count(*), B.productImage, B.productEfficacy " + "from recommendtbl A "
				+ "inner join products B " + "on B.productName = A.productName " + "group by productName "
				+ "order by count(*) desc " + "limit 3";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		RecommendVO recommendVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				recommendVO = new RecommendVO(rs.getString(1), rs.getInt(2), rs.getString(4), rs.getString(3));
				list.add(recommendVO);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return list;
	}
	// �� ���ƿ� ��� �̱�
	public ArrayList<ProductVO> getMyRecommend(String id) {
		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		String dml = "select B.* "
				+ "from recommendtbl A "
				+ "inner join products B "
				+ "on B.productName = A.productName "
				+ "where A.id = ?"
				+ "group by productName";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ProductVO productVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productVO = new ProductVO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),
						rs.getString(5),rs.getString(6));
				list.add(productVO);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return list;
	}
	
	// �̹� ��õ������ �ִ� ��ǰ���� Ȯ���ϴ� �޼ҵ�
	public int getDuplicateRecommend(String id, String productName) {
		int recommendCheck = 0;
		String dml = "select count(no) from recommendtbl where id = ? and productName = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			pstmt.setString(2, productName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				recommendCheck = rs.getInt(1);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e3) {
				e3.printStackTrace();
			}
		}
		return recommendCheck;
	}

}
