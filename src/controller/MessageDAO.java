package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.MessageVO;

public class MessageDAO {
	// ����ȭ�鿡�� �����ڿ��� �޼����� �������� �Ҷ� ���.
	public void getSendMessage(String id, String message) throws Exception {
		String dml = "insert into messagetbl (id, message) values (?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// DBUtil Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� ��ǰ ������ ó���ϱ� ���Ͽ� SQL������ ����
			pstmt = con.prepareStatement(dml); // =������ �غ��ϰڴ�. dml�� �־������ ?�� 1���� �ȴ�.

			pstmt.setString(1, id);
			pstmt.setString(2, message);

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
		return;
	}

	// ������ ��忡�� �����κ��� ���� �޼��� Ȯ���� �� �ְ� ��
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

	// ������ ��忡�� ���� �޼��� ����
	public void getMessageDelete(int no) throws Exception {
		// �� ������ ó���� ���� SQL ��
		String dml = "delete from messagetbl where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil�̶�� Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// SQL���� ������ ó�� ����� ����
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, String.valueOf(no));

			// SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "�޼��� ����", "�޼��� ���� �Ϸ�", "�޼��� ���� �����߽��ϴ�.");
			} else {
				DBUtil.alertDisplay(1, "�޼�������", "�޼��� ���� ����", "�޼��� ���� �����߽��ϴ�.");
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



}
