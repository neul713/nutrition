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
	  // �����ڸ�� ���� �α��� ��ư
      btnAdminOk.setOnAction(e -> {
         handlerBtnAdminOkAction(e);
      });
   }

   public void handlerBtnAdminOkAction(ActionEvent e) {
      AdminDAO adminDAO = new AdminDAO();
      
      if (txtAdminId.getText().equals("")) {
         DBUtil.alertDisplay(1, "������ ���̵� üũ", "������ ���̵� �Է� ���.", "������ ���̵� �Է����ּ���.");
         return;
      }
      if(txtAdminPassword.getText().equals("")) {
         DBUtil.alertDisplay(1, "������ ��й�ȣ ���Է�", "������ ��й�ȣ�� �Էµ��� �ʾҽ��ϴ�.", "������ ��й�ȣ�� �Է����ּ���.");
         return;
      }
      int idCheck = adminDAO.getAdminIdSearch(txtAdminId.getText());
      if (idCheck == 1) {
         // ���̵� �´� �н��������� üũ
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
                  // �α����� �Ϸ�Ǿ����� ������ȭ���� �θ�. �α���â ����.
                  Parent mainView = null;
                  Stage mainStage = null;
                  try {
                     mainView = FXMLLoader.load(getClass().getResource("/view/admin.fxml"));
                     Scene scene = new Scene(mainView);
                     mainStage = new Stage();
                     mainStage.setTitle("������ ���");
                     mainStage.setScene(scene);
                     mainStage.setResizable(true);
                     ((Stage) btnAdminOk.getScene().getWindow()).close();
                     mainStage.show();
                  } catch (IOException e1) {
                     DBUtil.alertDisplay(1, "�����ڸ�� �ݽ���", "������â �θ��� ����", e1.toString());
                  }         
               }else {
                  DBUtil.alertDisplay(1, "������ ��й�ȣ ����ġ", "��ġ�����ʴ� ��й�ȣ �Դϴ�.", "�ٽ� �Է����ּ���.");
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
         DBUtil.alertDisplay(1, "�α��� ����", "�������� �ʴ� ������ ���̵��Դϴ�.", " �ٽ� �Է����ּ���.");
         txtAdminId.clear();
         txtAdminPassword.clear();
         return;
      }
   }

      
   }
