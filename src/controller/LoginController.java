package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.LoginIdSave;
import model.UserVO;

public class LoginController implements Initializable {
	@FXML
	private ImageView loginImage;
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnSignUp;
	@FXML
	private Button btnAdmin;
	@FXML
	private Button btnIdPasswordFind;

	public static Stage mainStage = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 1. �α��� ��ư �̺�Ʈ ó��
		btnLogin.setOnAction(e -> {
			handlerBtnLoginAction(e);
		});
		// 2. ȸ������ ��ư �̺�Ʈ ó��
		btnSignUp.setOnAction(e -> {
			handlerBtnSignUpAction(e);
		});
		// 3. ������ ��ư �̺�Ʈ ó��
		btnAdmin.setOnAction(e -> {
			handlerBtnAdminAction(e);
		});
		// 4. ���̵�/��й�ȣ ã�� ��ư �̺�Ʈ ó��
		btnIdPasswordFind.setOnAction(e -> {
			handlerBtnIdPasswordFindAction(e);
		});

	}

	// 1. �α��� ��ư �̺�Ʈ ó��
	public void handlerBtnLoginAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		// �н����尡 �Էµ��� �ʾ����� ���â�� ����ش�
		if (txtId.getText().equals("")) {
			DBUtil.alertDisplay(1, "�α��� ����", "���̵� �Էµ��� �ʾҽ��ϴ�.", "���̵� �Է����ּ���.");
			return;
		}
		if (txtPassword.getText().equals("")) {
			DBUtil.alertDisplay(1, "�α��� ����", "��й�ȣ�� �Էµ��� �ʾҽ��ϴ�.", "��й�ȣ�� �Է����ּ���.");
			return;
		}

		int idCheck = userDAO.getUserIdSearch(txtId.getText());
		if (idCheck == 1) {
			// idCheck 1�̸� DB�� �����ϴ�, ȸ������ �Ǿ��ִ� ���̵��̱� ������ �� ���̵� �´� ��й�ȣ ���� Ȯ���Ѵ�
			LoginIdSave.saveId.add(txtId.getText());
			ArrayList<String> list = new ArrayList<String>();
			String dml = "select password from users where id = ?";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String id = txtId.getText();
			String password = null;
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(dml);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					password = rs.getString(1);
					list.add(password);
				}
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).equals(txtPassword.getText())) {
						// �α����� �Ϸ�Ǿ����� ����ȭ���� �θ�. �α���â ����.
						Parent mainView = null;
						try {
							mainView = FXMLLoader.load(getClass().getResource("/view/mainview.fxml"));
							Scene scene = new Scene(mainView);
							mainStage = new Stage();
							mainStage.setTitle("����");
							mainStage.setScene(scene);
							mainStage.setResizable(true);
							((Stage) btnLogin.getScene().getWindow()).close();
							mainStage.show();
						} catch (IOException e1) {
							DBUtil.alertDisplay(1, "���� ȭ�� �θ���", "���� ȭ���� �ҷ����� ���߽��ϴ�.", e1.toString());
							e1.printStackTrace();
						}
					} else {
						DBUtil.alertDisplay(1, "�α��� ����", "��й�ȣ�� ��ġ�����ʽ��ϴ�.", "�ٽ� Ȯ�����ּ���.");
						txtPassword.clear();
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
			// idCheck�� -1�̸� DB�� �Է��� ���̵� �������� �ʴ´ٴ� ��.
			DBUtil.alertDisplay(1, "�α��� ����", "�������� �ʴ� ���̵��Դϴ�.", "�ٽ� Ȯ�����ּ���.");
			txtId.clear();
			txtPassword.clear();
			return;
		}
	}

	// 2. ȸ������ ��ư �̺�Ʈ ó��
	public void handlerBtnSignUpAction(ActionEvent e) {
		Parent signUpView = null;
		Stage signUpStage = null;
		try {
			signUpView = FXMLLoader.load(getClass().getResource("/view/signup.fxml"));
			Scene scene = new Scene(signUpView);
			signUpStage = new Stage();
			signUpStage.setTitle("ȸ������");
			signUpStage.setScene(scene);
			signUpStage.setResizable(true);
			signUpStage.show();
		} catch (IOException e2) {
			DBUtil.alertDisplay(1, "ȸ������ ����", "ȸ������ ȭ���� �ҷ����� ���߽��ϴ�.", "�ٽ� Ȯ�����ּ���.");
		}
	}

	// 3. ������ ��ư �̺�Ʈ ó��
	public void handlerBtnAdminAction(ActionEvent e) {
		Parent adminView = null;
		Stage adminStage = null;
		try {
			adminView = FXMLLoader.load(getClass().getResource("/view/adminentry.fxml"));
			Scene scene = new Scene(adminView);
			adminStage = new Stage();
			adminStage.setTitle("������ ���");
			adminStage.setScene(scene);
			adminStage.setResizable(true);
			adminStage.show();
		} catch (IOException e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "������ ��� ����", "������ ��� ȭ���� �ҷ����� ���߽��ϴ�.", "�ٽ� Ȯ�����ּ���.");
		}
	}

	// 4. ���̵�/��й�ȣ ã�� ��ư �̺�Ʈ ó��
	public void handlerBtnIdPasswordFindAction(ActionEvent e) {
		Parent findView = null;
		Stage findStage = null;
		try {
			findView = FXMLLoader.load(getClass().getResource("/view/idpasswordfind.fxml"));
			Scene scene = new Scene(findView);
			findStage = new Stage();
			findStage.setTitle("���̵�, ��й�ȣ ã��");
			findStage.setScene(scene);
			findStage.setResizable(true);
			findStage.show();
		} catch (IOException e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "���̵�, ��й�ȣ ã�� â  �θ��� ����", "���̵�, ��й�ȣ ã�� ���  �θ��� ����", e2.toString());
		}
	}

}
