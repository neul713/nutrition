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
		// 1. 로그인 버튼 이벤트 처리
		btnLogin.setOnAction(e -> {
			handlerBtnLoginAction(e);
		});
		// 2. 회원가입 버튼 이벤트 처리
		btnSignUp.setOnAction(e -> {
			handlerBtnSignUpAction(e);
		});
		// 3. 관리자 버튼 이벤트 처리
		btnAdmin.setOnAction(e -> {
			handlerBtnAdminAction(e);
		});
		// 4. 아이디/비밀번호 찾기 버튼 이벤트 처리
		btnIdPasswordFind.setOnAction(e -> {
			handlerBtnIdPasswordFindAction(e);
		});

	}

	// 1. 로그인 버튼 이벤트 처리
	public void handlerBtnLoginAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		// 패스워드가 입력되지 않았을때 경고창을 띄워준다
		if (txtId.getText().equals("")) {
			DBUtil.alertDisplay(1, "로그인 오류", "아이디가 입력되지 않았습니다.", "아이디를 입력해주세요.");
			return;
		}
		if (txtPassword.getText().equals("")) {
			DBUtil.alertDisplay(1, "로그인 오류", "비밀번호가 입력되지 않았습니다.", "비밀번호를 입력해주세요.");
			return;
		}

		int idCheck = userDAO.getUserIdSearch(txtId.getText());
		if (idCheck == 1) {
			// idCheck 1이면 DB에 존재하는, 회원가입 되어있는 아이디이기 때문에 그 아이디에 맞는 비밀번호 인지 확인한다
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
						// 로그인이 완료되었으면 메인화면을 부름. 로그인창 꺼짐.
						Parent mainView = null;
						try {
							mainView = FXMLLoader.load(getClass().getResource("/view/mainview.fxml"));
							Scene scene = new Scene(mainView);
							mainStage = new Stage();
							mainStage.setTitle("메인");
							mainStage.setScene(scene);
							mainStage.setResizable(true);
							((Stage) btnLogin.getScene().getWindow()).close();
							mainStage.show();
						} catch (IOException e1) {
							DBUtil.alertDisplay(1, "메인 화면 부르기", "메인 화면을 불러오지 못했습니다.", e1.toString());
							e1.printStackTrace();
						}
					} else {
						DBUtil.alertDisplay(1, "로그인 오류", "비밀번호가 일치하지않습니다.", "다시 확인해주세요.");
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
			// idCheck가 -1이면 DB에 입력한 아이디가 존재하지 않는다는 것.
			DBUtil.alertDisplay(1, "로그인 오류", "존재하지 않는 아이디입니다.", "다시 확인해주세요.");
			txtId.clear();
			txtPassword.clear();
			return;
		}
	}

	// 2. 회원가입 버튼 이벤트 처리
	public void handlerBtnSignUpAction(ActionEvent e) {
		Parent signUpView = null;
		Stage signUpStage = null;
		try {
			signUpView = FXMLLoader.load(getClass().getResource("/view/signup.fxml"));
			Scene scene = new Scene(signUpView);
			signUpStage = new Stage();
			signUpStage.setTitle("회원가입");
			signUpStage.setScene(scene);
			signUpStage.setResizable(true);
			signUpStage.show();
		} catch (IOException e2) {
			DBUtil.alertDisplay(1, "회원가입 오류", "회원가입 화면을 불러오지 못했습니다.", "다시 확인해주세요.");
		}
	}

	// 3. 관리자 버튼 이벤트 처리
	public void handlerBtnAdminAction(ActionEvent e) {
		Parent adminView = null;
		Stage adminStage = null;
		try {
			adminView = FXMLLoader.load(getClass().getResource("/view/adminentry.fxml"));
			Scene scene = new Scene(adminView);
			adminStage = new Stage();
			adminStage.setTitle("관리자 모드");
			adminStage.setScene(scene);
			adminStage.setResizable(true);
			adminStage.show();
		} catch (IOException e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "관리자 모드 오류", "관리자 모드 화면을 불러오지 못했습니다.", "다시 확인해주세요.");
		}
	}

	// 4. 아이디/비밀번호 찾기 버튼 이벤트 처리
	public void handlerBtnIdPasswordFindAction(ActionEvent e) {
		Parent findView = null;
		Stage findStage = null;
		try {
			findView = FXMLLoader.load(getClass().getResource("/view/idpasswordfind.fxml"));
			Scene scene = new Scene(findView);
			findStage = new Stage();
			findStage.setTitle("아이디, 비밀번호 찾기");
			findStage.setScene(scene);
			findStage.setResizable(true);
			findStage.show();
		} catch (IOException e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "아이디, 비밀번호 찾기 창  부르기 실패", "아이디, 비밀번호 찾기 모드  부르기 실패", e2.toString());
		}
	}

}
