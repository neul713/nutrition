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
	// �α��� ȭ�鿡�� ���̵� ã��
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

	// �α��� ȭ�鿡�� ��й�ȣ ã��
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

	// �����ڸ�忡�� ���� ��� �м��ؼ� ��Ʈ.
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

	// ȸ�� ���
	public void getUserRegiste(UserVO uvo) throws Exception {

		String dml = "insert into users " + "(id, password, passwordCheck, name, birth, phoneNumber, survey, userImage)"
				+ " values " + "(?, ?, ?, ?, ?, ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� �л� ������ ó���ϱ� ���Ͽ� SQL������ ����
			pstmt = con.prepareStatement(dml); // =������ �غ��ϰڴ�. dml�� �־������ ?�� 1���� �ȴ�.
			pstmt.setString(1, uvo.getId());
			pstmt.setString(2, uvo.getPassword());
			pstmt.setString(3, uvo.getPasswordCheck());
			pstmt.setString(4, uvo.getName());
			pstmt.setString(5, uvo.getBirth());
			pstmt.setString(6, uvo.getPhoneNumber());
			pstmt.setString(7, uvo.getSurvey());
			pstmt.setString(8, uvo.getUserImage());

			// SQL���� ������ ó�� ����� ����
			pstmt.execute();

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

	// �����ڸ�忡�� �̸����� ȸ�� �˻�
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

	// �������������� �� ���� ��������
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

	// ȸ�� ��ü ��������
	public ArrayList<UserVO> getUserTotal() {
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		String dml = "select * from users";

		Connection con = null;
		PreparedStatement pstmt = null;
		// �����ͺ��̽����� �������� ���� �ӽ÷� �����ϴ� ��� �����ϴ� ��ü
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

	// �������������� �� ���� �����Ҷ�
	public void getUserDelete(String id) throws Exception {
		// ������ ó���� ���� SQL ��
		String dml = "delete from users where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil�̶�� Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// SQL���� ������ ó�� ����� ����
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);

			// SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "ȸ�� ���� ����/����", "ȸ�� ���� ����/���� �Ϸ�", "ȸ�� ���� ����/���� �����߽��ϴ�.");
			} else {
				DBUtil.alertDisplay(1, "ȸ�� ���� ����/����", "ȸ�� ���� ����/���� ����", "ȸ�� ���� ����/���� �����߽��ϴ�.");
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

	// ȸ�� Ż��
	public void getUserSecession(String id) throws Exception {
		// ������ ó���� ���� SQL ��
		String dml = "delete from users where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil�̶�� Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();
			// SQL���� ������ ó�� ����� ����
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			// SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "ȸ�� Ż��", "ȸ�� Ż�� �Ϸ�", "Ż��Ǿ����ϴ�. �� ã���ּ����0��");
			} else {
				DBUtil.alertDisplay(1, "ȸ�� Ż��", "ȸ�� Ż�� ����", "ȸ�� Ż�� �����Ͽ����ϴ�.");
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

	// ���̵� �ߺ� Ȯ��
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
				DBUtil.alertDisplay(1, "�ߺ� Ȯ��", "���̵� �Էµ��� �ʾҽ��ϴ�.", "���̵� �Է����ּ���.");
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
