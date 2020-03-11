package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.ProductVO;

public class ProductDAO {
	// ������ ��忡�� ��ǰ ���
	public void getProductRegiste(ProductVO pvo) throws Exception {
		String dml = "insert into products "
				+ "(productName , productPrice, productEfficacy, productCompany, productIngredient, productImage" + ")"
				+ " values " + "(?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// DBUtil Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� ��ǰ ������ ó���ϱ� ���Ͽ� SQL������ ����
			pstmt = con.prepareStatement(dml); // =������ �غ��ϰڴ�. dml�� �־������ ?�� 1���� �ȴ�.

			pstmt.setString(1, pvo.getProductName());
			pstmt.setString(2, pvo.getProductPrice());
			pstmt.setString(3, pvo.getProductEfficacy());
			pstmt.setString(4, pvo.getProductCompany());
			pstmt.setString(5, pvo.getProductIngredient());
			pstmt.setString(6, pvo.getProductImage());

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

	// ����ȭ�鿡�� ��ǰ������ �˻�
	public ArrayList<ProductVO> getProductCheck(String productName) {
		ArrayList<ProductVO> list2 = new ArrayList<ProductVO>();
		String dml = "select * from products where productName like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO retval = null;

		String productName2 = "%" + productName + "%";
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, productName2);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new ProductVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6));
				list2.add(retval);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list2;
	}

	// ����ȭ�鿡�� ��ǰ ȿ������ �˻�
	public ArrayList<ProductVO> getProductEfficacyCheck(String productEfficacy) {
		ArrayList<ProductVO> list2 = new ArrayList<ProductVO>();
		String dml = "select * from products where productEfficacy = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO retval = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, productEfficacy);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new ProductVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6));
				list2.add(retval);
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
		return list2;
	}

	// ��ǰ��� ��ü ��������
	public ArrayList<ProductVO> getProductTotal() {
		ArrayList<ProductVO> list2 = new ArrayList<ProductVO>();
		String dml = "select * from products";

		Connection con = null;
		PreparedStatement pstmt = null;
		// �����ͺ��̽����� �������� ���� �ӽ÷� �����ϴ� ��� �����ϴ� ��ü
		ResultSet rs = null;
		ProductVO productVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productVO = new ProductVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6));
				list2.add(productVO);
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
		return list2;
	}

	// �����ڿ��� ��ǰ �����Ҷ�
	public void getProductDelete(String productName) throws Exception {
		// ������ ó���� ���� SQL ��
		String dml = "delete from products where productName = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil�̶�� Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// SQL���� ������ ó�� ����� ����
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, productName);

			// SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "��ǰ ���� ����/����", "��ǰ ���� ����/���� �Ϸ�", "��ǰ ���� ����/���� �����߽��ϴ�.");
			} else {
				DBUtil.alertDisplay(1, "��ǰ ���� ����/����", "��ǰ ���� ����/���� ����", "��ǰ ���� ����/���� �����߽��ϴ�.");
			}

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
	}

	// ������ ��忡�� ��ǰ��� �� �� ��ǰ �̸� �ߺ� Ȯ��
	public int getProductNameSearch(String productName) {
		String saveProductName = null;
		ArrayList<String> list = new ArrayList<String>();
		String dml = "select productName from products where productName = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, productName);
			rs = pstmt.executeQuery();
			if (productName.equals("")) {
				DBUtil.alertDisplay(1, "�ߺ� Ȯ��", "��ǰ �̸��� �Էµ��� �ʾҽ��ϴ�.", "�ٽ� Ȯ�����ּ���.");
				return 0;
			}
			while (rs.next()) {
				saveProductName = rs.getString(1);
				list.add(saveProductName);
			}
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(productName)) {
					return 1;
				}
			}
			return -1;
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
		return 0;
	}
}