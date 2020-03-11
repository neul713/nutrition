package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminEntryController implements Initializable {
   @FXML
   private TextField txtAdminId;
   @FXML
   private TextField txtAdminPassword;
   @FXML
   private Button btnAdminOk;

   @Override
   public void initialize(URL arg0, ResourceBundle arg1) {
	  // 관리자모드 들어갈때 로그인 버튼
      btnAdminOk.setOnAction(e -> {
         handlerBtnAdminOkAction(e);
      });
   }

   public void handlerBtnAdminOkAction(ActionEvent e) {
      AdminDAO adminDAO = new AdminDAO();
      
      if (txtAdminId.getText().equals("")) {
         DBUtil.alertDisplay(1, "관리자 아이디 체크", "관리자 아이디 입력 요망.", "관리자 아이디를 입력해주세요.");
         return;
      }
      if(txtAdminPassword.getText().equals("")) {
         DBUtil.alertDisplay(1, "관리자 비밀번호 미입력", "관리자 비밀번호가 입력되지 않았습니다.", "관리자 비밀번호를 입력해주세요.");
         return;
      }
      int idCheck = adminDAO.getAdminIdSearch(txtAdminId.getText());
      if (idCheck == 1) {
         // 아이디에 맞는 패스워드인지 체크
         ArrayList<String> list = new ArrayList<String>();
         String dml = "select adminPassword from admin where adminId = ?";
         Connection con = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         String adminId = txtAdminId.getText();
         String adminPassword = null;
         try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(dml);
            pstmt.setString(1, adminId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
               adminPassword = rs.getString(1);
               list.add(adminPassword);
            }
            for (int i = 0; i < list.size(); i++) {
               if (list.get(i).equals(txtAdminPassword.getText())) {
                  // 로그인이 완료되었으면 관리자화면을 부름. 로그인창 꺼짐.
                  Parent mainView = null;
                  Stage mainStage = null;
                  try {
                     mainView = FXMLLoader.load(getClass().getResource("/view/admin.fxml"));
                     Scene scene = new Scene(mainView);
                     mainStage = new Stage();
                     mainStage.setTitle("관리자 모드");
                     mainStage.setScene(scene);
                     mainStage.setResizable(true);
                     ((Stage) btnAdminOk.getScene().getWindow()).close();
                     mainStage.show();
                  } catch (IOException e1) {
                     DBUtil.alertDisplay(1, "관리자모드 콜실패", "관리자창 부르기 실패", e1.toString());
                  }         
               }else {
                  DBUtil.alertDisplay(1, "관리자 비밀번호 불일치", "일치하지않는 비밀번호 입니다.", "다시 입력해주세요.");
                  txtAdminPassword.clear();
               }
            }
         } catch (SQLException se) {
            System.out.println(se);
         } catch (Exception e2) {
            System.out.println(e2);
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
      } else if (idCheck == -1) {
         DBUtil.alertDisplay(1, "로그인 실패", "존재하지 않는 관리자 아이디입니다.", " 다시 입력해주세요.");
         txtAdminId.clear();
         txtAdminPassword.clear();
         return;
      }
   }

      
   }
