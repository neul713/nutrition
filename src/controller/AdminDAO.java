package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAO {
	//관리자 아이디 확인
   public int getAdminIdSearch(String adminId) {
      String saveAdminId = null;
      ArrayList<String> list = new ArrayList<String>();
      String dml = "select adminId from admin where adminId = ?";
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         con = DBUtil.getConnection();
         pstmt = con.prepareStatement(dml);
         pstmt.setString(1, adminId);
         rs = pstmt.executeQuery();
         if (adminId.equals("")) {
            DBUtil.alertDisplay(1, "관리자 아이디 체크", "관리자 아이디 입력 요망.", " 관리자 아이디를 입력해주세요.");
            return 0;
         }
         while (rs.next()) {
            saveAdminId = rs.getString(1);
            list.add(saveAdminId);
         }
         for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(adminId)) {
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