package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.MessageVO;

public class MessageDAO {
	// 메인화면에서 관리자에게 메세지를 보내고자 할때 사용.
	public void getSendMessage(String id, String message) throws Exception {
		String dml = "insert into messagetbl (id, message) values (?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 제품 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // =문장을 준비하겠다. dml을 넣어버리고 ?가 1번이 된다.

			pstmt.setString(1, id);
			pstmt.setString(2, message);

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt.execute();

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

	// 관리자 모드에서 유저로부터 받은 메세지 확인할 수 있게 함
	public ArrayList<MessageVO> getMessageCheck() {
		ArrayList<MessageVO> list = new ArrayList<MessageVO>();
		String dml = "select * from messagetbl";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		MessageVO messageVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				messageVO = new MessageVO(rs.getInt(1), rs.getString(2),rs.getString(3));
				list.add(messageVO);
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

	// 관리자 모드에서 받은 메세지 삭제
	public void getMessageDelete(int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "delete from messagetbl where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, String.valueOf(no));

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "메세지 삭제", "메세지 삭제 완료", "메세지 삭제 성공했습니다.");
			} else {
				DBUtil.alertDisplay(1, "메세지삭제", "메세지 삭제 실패", "메세지 삭제 실패했습니다.");
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



}
