package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Platform;
import model.PieChartVO;
import model.UserVO;

public class UserDAO {
	// 로그인 화면에서 아이디 찾기
	public String getUserIdFind(String name, String birth) {
		String saveId = null;
		String dml = " select id from users where name = ? and birth = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, name);
			pstmt.setString(2, birth);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				saveId = rs.getString(1);
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
		return saveId;
	}

	// 로그인 화면에서 비밀번호 찾기
	public String getUserPasswordFind(String id, String phoneNumber) {
		String savePassword = null;
		String dml = " select Password from users where id = ? and phoneNumber = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			pstmt.setString(2, phoneNumber);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				savePassword = rs.getString(1);

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
		return savePassword;
	}

	// 관리자모드에서 유입 경로 분석해서 차트.
	public ArrayList<PieChartVO> getSurveyResult() {
		ArrayList<PieChartVO> list = new ArrayList<PieChartVO>();
		String dml = "select survey, count(survey) from users group by survey";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		PieChartVO pieChartVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pieChartVO = new PieChartVO(rs.getString(1), rs.getInt(2));
				list.add(pieChartVO);
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

		return list;
	}

	// 회원 등록
	public void getUserRegiste(UserVO uvo) throws Exception {

		String dml = "insert into users " + "(id, password, passwordCheck, name, birth, phoneNumber, survey, userImage)"
				+ " values " + "(?, ?, ?, ?, ?, ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // =문장을 준비하겠다. dml을 넣어버리고 ?가 1번이 된다.
			pstmt.setString(1, uvo.getId());
			pstmt.setString(2, uvo.getPassword());
			pstmt.setString(3, uvo.getPasswordCheck());
			pstmt.setString(4, uvo.getName());
			pstmt.setString(5, uvo.getBirth());
			pstmt.setString(6, uvo.getPhoneNumber());
			pstmt.setString(7, uvo.getSurvey());
			pstmt.setString(8, uvo.getUserImage());

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
	}

	// 관리자모드에서 이름으로 회원 검색
	public ArrayList<UserVO> getUserCheck(String name) {
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		String dml = "select * from users where name like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO retval = null;

		String name2 = "%" + name + "%";
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, name2);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new UserVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8));
				list.add(retval);
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
		return list;
	}

	// 마이페이지에서 내 정보 가져오기
	public ArrayList<UserVO> getUserCheck2(String id) {
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		String dml = "select * from users where id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO retval = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new UserVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8));
				list.add(retval);
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
		return list;
	}

	// 회원 전체 가져오기
	public ArrayList<UserVO> getUserTotal() {
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		String dml = "select * from users";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이터베이스에서 가져오는 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;

		UserVO userVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userVO = new UserVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8));
				list.add(userVO);
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
		return list;
	}

	// 마이페이지에서 내 정보 수정할때
	public void getUserDelete(String id) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "delete from users where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "회원 정보 수정/삭제", "회원 정보 수정/삭제 완료", "회원 정보 수정/삭제 성공했습니다.");
			} else {
				DBUtil.alertDisplay(1, "회원 정보 수정/삭제", "회원 정보 수정/삭제 실패", "회원 정보 수정/삭제 실패했습니다.");
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

	// 회원 탈퇴
	public void getUserSecession(String id) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "delete from users where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "회원 탈퇴", "회원 탈퇴 완료", "탈퇴되었습니다. 또 찾아주세요ㅠ0ㅠ");
			} else {
				DBUtil.alertDisplay(1, "회원 탈퇴", "회원 탈퇴 실패", "회원 탈퇴에 실패하였습니다.");
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

	// 아이디 중복 확인
	public int getUserIdSearch(String id) {
		String saveId = null;
		ArrayList<String> list = new ArrayList<String>();
		String dml = "select id from users where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (id.equals("")) {
				DBUtil.alertDisplay(1, "중복 확인", "아이디가 입력되지 않았습니다.", "아이디를 입력해주세요.");
				return 0;
			}
			while (rs.next()) {
				saveId = rs.getString(1);
				list.add(saveId);
			}
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(id)) {
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
