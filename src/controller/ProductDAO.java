package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.ProductVO;

public class ProductDAO {
	// 관리자 모드에서 제품 등록
	public void getProductRegiste(ProductVO pvo) throws Exception {
		String dml = "insert into products "
				+ "(productName , productPrice, productEfficacy, productCompany, productIngredient, productImage" + ")"
				+ " values " + "(?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 제품 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // =문장을 준비하겠다. dml을 넣어버리고 ?가 1번이 된다.

			pstmt.setString(1, pvo.getProductName());
			pstmt.setString(2, pvo.getProductPrice());
			pstmt.setString(3, pvo.getProductEfficacy());
			pstmt.setString(4, pvo.getProductCompany());
			pstmt.setString(5, pvo.getProductIngredient());
			pstmt.setString(6, pvo.getProductImage());

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt.execute();// 몇문장을 실행했는지

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return;
	}

	// 메인화면에서 제품명으로 검색
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

	// 메인화면에서 제품 효능으로 검색
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

	// 제품목록 전체 가져오기
	public ArrayList<ProductVO> getProductTotal() {
		ArrayList<ProductVO> list2 = new ArrayList<ProductVO>();
		String dml = "select * from products";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이터베이스에서 가져오는 값을 임시로 저장하는 장소 제공하는 객체
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

	// 관리자에서 제품 삭제할때
	public void getProductDelete(String productName) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "delete from products where productName = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, productName);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "제품 정보 수정/삭제", "제품 정보 수정/삭제 완료", "제품 정보 수정/삭제 성공했습니다.");
			} else {
				DBUtil.alertDisplay(1, "제품 정보 수정/삭제", "제품 정보 수정/삭제 실패", "제품 정보 수정/삭제 실패했습니다.");
			}

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}

	// 관리자 모드에서 제품등록 할 때 제품 이름 중복 확인
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
				DBUtil.alertDisplay(1, "중복 확인", "제품 이름이 입력되지 않았습니다.", "다시 확인해주세요.");
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