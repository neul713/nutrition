package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import application.AppMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.LoginIdSave;
import model.UserVO;

public class MyPageController implements Initializable {
	@FXML
	private TextField txtIdEntryCheck;
	@FXML
	private PasswordField txtPasswordEntryCheck;
	@FXML
	private Button btnMyPageCheck;
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private PasswordField txtPasswordCheck;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtBirth;
	@FXML
	private TextField txtPhoneNumber;
	@FXML
	private TextField txtSurbey;
	@FXML
	private ImageView imgUserPhoto;
	@FXML
	private Button btnDuplicate;
	@FXML
	private Button btnUserPhotoUp;
	@FXML
	private Button btnUserPhotoDown;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnUserSecession;
	private ObservableList<UserVO> data;
	private UserDAO userDAO;
	private File selectedFile = null;
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	// 이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("C:/userImages");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;
	private String fileName;

	// 사진 삭제 버튼 누르고 새로 사진을 등록하지않으면 정보 변경할 수 없게하는 변수..
	private boolean photoDelete = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 로그인한 아이디 받아오기
		result();
		// 보안을 위한 아이디 비밀번호 확인전에는 사용할 수 없게 함.
		myPageSetting();
		// 가입정보 제약조건
		textSetting();
		// 내 정보 가져오기
		btnMyPageCheck.setOnAction(e -> {
			handlerBtnMyPageCheckAction(e);
		});
		// 사진 등록 버튼
		btnUserPhotoUp.setOnAction(e -> {
			handlerBtnUserPhotoUpAction(e);
		});
		// 사진 삭제 버튼
		btnUserPhotoDown.setOnAction(e -> {
			handlerBtnImageFileAction(e);
		});
		// 수정 완료 버튼 이벤트 처리
		btnEdit.setOnAction(e -> {
			handlerBtnEditAction(e);
		});
		// 탈퇴 버튼 이벤트 처리
		btnUserSecession.setOnAction(e -> {
			handlerBtnUserSecessionAction(e);
		});
	}
	// 로그인한 아이디 받아오기
	public void result() {
		txtIdEntryCheck.setText(LoginIdSave.saveId.get(0));
	}

	// 보안을 위한 아이디 비밀번호 확인전에는 사용할 수 없게 함.
	public void myPageSetting() {
		txtId.setDisable(true);
		txtPassword.setDisable(true);
		txtPasswordCheck.setDisable(true);
		txtName.setDisable(true);
		txtBirth.setDisable(true);
		txtPhoneNumber.setDisable(true);
		txtSurbey.setDisable(true);
		btnUserPhotoUp.setDisable(true);
		btnUserPhotoDown.setDisable(true);
		btnEdit.setDisable(true);
		btnUserSecession.setDisable(true);
	}

	// 가입정보 제약조건
	public void textSetting() {
		DecimalFormat format = new DecimalFormat("###########");
		txtPhoneNumber.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 12) {
				return null;
			} else {
				return event;
			}
		}));

	}

	// 내 정보 가져오기
	public void handlerBtnMyPageCheckAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		int idCheck = userDAO.getUserIdSearch(txtIdEntryCheck.getText());
		if (idCheck == 1) {
			ArrayList<String> list = new ArrayList<String>();
			String dml = "select password from users where id = ?";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String id = txtIdEntryCheck.getText();
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
					if (list.get(i).equals(txtPasswordEntryCheck.getText())) {
						// 보안 로그인 확인 후에 수정할 수 있는 텍스트필드와 버튼 사용할 수 있게 풀어줌.
						txtPassword.setDisable(false);
						txtPasswordCheck.setDisable(false);
						txtPhoneNumber.setDisable(false);
						btnUserPhotoUp.setDisable(false);
						btnUserPhotoDown.setDisable(false);
						btnEdit.setDisable(false);
						btnUserSecession.setDisable(false);
						// 로그인이 완료되었으면 내 정보 세팅해줌
						ArrayList<UserVO> list2 = null;
						UserDAO uDAO = new UserDAO();
						UserVO userVO = null;

						list2 = uDAO.getUserCheck2(txtIdEntryCheck.getText());
						if (list2 == null) {
							DBUtil.alertDisplay(1, "경고", "데이터베이스 가져오기 오류", "점검요망");
							return;
						}

						for (int j = 0; j < list2.size(); j++) {
							userVO = list2.get(j);
						}

						txtId.setText(userVO.getId());
						txtPassword.setText(userVO.getPassword());
						txtPasswordCheck.setText(userVO.getPasswordCheck());
						txtName.setText(userVO.getName());
						txtBirth.setText(userVO.getBirth());
						txtPhoneNumber.setText(userVO.getPhoneNumber());
						txtSurbey.setText(userVO.getSurvey());

						String fileName = userVO.getUserImage();
						selectedFile = new File("C:/userImages/" + fileName);
						if (selectedFile != null) {
							// 이미지 파일 경로
							try {
								localUrl = selectedFile.toURI().toURL().toString();
								localImage = new Image(localUrl, false);
								imgUserPhoto.setImage(localImage);
								imgUserPhoto.setFitHeight(230);
								imgUserPhoto.setFitWidth(200);
								imgUserPhoto.setLayoutX(380);
								imgUserPhoto.setLayoutY(260);
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}
						}
					} else {
						DBUtil.alertDisplay(1, "로그인 오류", "비밀번호가 일치하지 않습니다.", "다시 입력해주세요.");
						txtPasswordEntryCheck.clear();
					}
				}
			} catch (Exception se) {
				se.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (Exception se) {
				}
			}
		} else if (idCheck == -1) {
			DBUtil.alertDisplay(1, "로그인 오류", "존재하지 않는 아이디입니다.", "다시 확인해주세요.");
			txtIdEntryCheck.clear();
			txtPasswordEntryCheck.clear();
			return;
		}
	}

	// 사진 등록 버튼
	public void handlerBtnUserPhotoUpAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		try {
			selectedFile = fileChooser.showOpenDialog(btnUserPhotoUp.getScene().getWindow());
			if (selectedFile != null) {
				// 이미지 파일 경로
				localUrl = selectedFile.toURI().toURL().toString();
			}
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		localImage = new Image(localUrl, false);
		imgUserPhoto.setImage(localImage);
		imgUserPhoto.setLayoutX(381);
		imgUserPhoto.setLayoutY(259);
		imgUserPhoto.setFitHeight(260);
		imgUserPhoto.setFitWidth(193);
		btnUserPhotoUp.setDisable(false);
		photoDelete = false;
	}

	// 사진 삭제 버튼
	public void handlerBtnImageFileAction(ActionEvent e) {
		fileName = imageSave(null);
		imgUserPhoto.setImage(null);
		photoDelete = true;
	}

	// 수정 완료 버튼 이벤트 처리
	public void handlerBtnEditAction(ActionEvent e) {
		File dirMake = new File(dirSave.getAbsolutePath());
		// 이미지 저장 폴더 생성
		if (!dirMake.exists()) {
			dirMake.mkdir();
		}
		if (photoDelete == true) {
			DBUtil.alertDisplay(1, "등록 실패", "사진이 등록되지 않았습니다", "다시 확인해주세요.");
			return;
		}
		// 이미지 파일 저장
		fileName = imageSave(selectedFile);
		try {
			if (txtId.getText().equals("") || txtPassword.getText().equals("") || txtPasswordCheck.getText().equals("")
					|| txtName.getText().equals("") || txtBirth.equals("") || txtPhoneNumber.getText().equals("")
					|| txtSurbey.equals("") || fileName == null) {
				DBUtil.alertDisplay(1, "정보 수정", "입력하지 않은 항목이 있습니다.", "다시 확인해주세요.");
				throw new Exception();
			} else if (!(txtPassword.getText().equals(txtPasswordCheck.getText()))) {
				DBUtil.alertDisplay(1, "정보 수정 오류", "비밀번호가 일치하지 않습니다.", "다시 입력해주세요.");
				txtPasswordCheck.clear();
				throw new Exception();
			} else {
				UserVO uvo = new UserVO(txtId.getText(), txtPassword.getText(), txtPasswordCheck.getText(),
						txtName.getText(), txtBirth.getText(), txtPhoneNumber.getText(), txtSurbey.getText(), fileName);
				// DB를 부르는 명령
				userDAO = new UserDAO();
				// 데이터베이스 테이블에 입력값을 입력하는 함수
				userDAO.getUserDelete(uvo.getId());
				userDAO.getUserRegiste(uvo);
				// 알림창  띄우기
				((Stage) btnEdit.getScene().getWindow()).close();
				DBUtil.alertDisplay(1, "정보 수정", "회원 정보가 수정되었습니다.", "즐거운 이용 되세요^0^");
			}
		} catch (Exception e1) {

		}
	}

	// 탈퇴 버튼 이벤트 처리
	public void handlerBtnUserSecessionAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("탈퇴");
			alert.setHeaderText("탈퇴 확인");
			alert.setContentText("정말 탈퇴하시겠습니까?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				userDAO.getUserSecession(txtId.getText());
				((Stage) btnUserSecession.getScene().getWindow()).close();
				LoginController.mainStage.close();
				Parent returnRoot = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
				Scene scene = new Scene(returnRoot);
				AppMain.mainStage.setScene(scene);
				AppMain.mainStage.show();

			} else {

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// 이미지 파일명 생성
			fileName = "user" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

			// 선택한 이미지 파일 InputStream의 마지막에 이르렀을 경우는 -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return fileName;
	}

}