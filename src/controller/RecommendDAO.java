package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ProductVO;
import model.RecommendVO;

public class RecommendDAO {
	// 메인화면에서 추천 버튼을 누르면 실행되는 메소드
	public void getRecommendRegiste(String id, String productName) throws Exception {
		String dml = "insert into recommendtbl " + "(id, productName) " + "values " + "(?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 제품 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // =문장을 준비하겠다. dml을 넣어버리고 ?가 1번이 된다.

			pstmt.setString(1, id);
			pstmt.setString(2, productName);
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

	// 메인화면에 있는 TOP3 세팅
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
	// 내 좋아요 목록 뽑기
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
	
	// 이미 추천한적이 있는 제품인지 확인하는 메소드
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
