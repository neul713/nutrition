package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.UserVO;

public class SignUpController implements Initializable {
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField txtPasswordCheck;
	@FXML
	private TextField txtName;
	@FXML
	private DatePicker dpBirth;
	@FXML
	private TextField txtPhoneNumber;
	@FXML
	private ComboBox<String> cbSurvey;
	@FXML
	private ImageView imgUserPhoto;
	@FXML
	private Button btnDuplicate;
	@FXML
	private Button btnUserPhotoUp;
	@FXML
	private Button btnUserPhotoDown;
	@FXML
	private Button btnSignUp;
	// 유입경로 콤보박스
	ObservableList<String> survey;

	@FXML
	private TableView<UserVO> tableView = new TableView<>();
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

	// 사진 삭제 버튼 누르면 사진이 없으니께 사진 등록 새로 하던가 회원가입 안되게 하는 변수
	private boolean photoDelete = false;
	// 회원가입에 사용되는 변수..
	private boolean idCheck = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 가입정보 제약조건
		textSetting();
		// 유입경로 콤보박스
		survey = FXCollections.observableArrayList("인터넷 검색", "지인 소개", "유튜브/광고", "SNS");
		cbSurvey.setItems(survey);
		// 중복확인 버튼 이벤트 처리
		btnDuplicate.setOnAction(e -> {
			handlerBtnDuplicateAction(e);
		});
		// 사진 등록 버튼
		btnUserPhotoUp.setOnAction(e -> {
			handlerBtnUserPhotoUpAction(e);
		});
		// 사진 삭제 버튼
		btnUserPhotoDown.setOnAction(e -> {
			handlerBtnImageFileAction(e);
		});
		// 회원가입 버튼 이벤트 처리
		btnSignUp.setOnAction(e -> {
			handlerBtnSignUpAction(e);
		});
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

	// 중복확인 버튼 이벤트 처리
	public void handlerBtnDuplicateAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		int i = userDAO.getUserIdSearch(txtId.getText());
		idCheck = true;
		if (i == 1) {
			DBUtil.alertDisplay(1, "아이디 중복", "이미 사용중인 아이디입니다.", "다른 아이디를 입력해주세요.");
			btnSignUp.setDisable(true);
			return;
		} else if (i == -1) {
			DBUtil.alertDisplay(1, "아이디 사용가능", "사용할 수 있는 아이디입니다.", "다른 항목도 입력해주세요.");
			btnSignUp.setDisable(false);
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
		imgUserPhoto.setLayoutY(42);
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

	// 회원가입 버튼 이벤트 처리
	public void handlerBtnSignUpAction(ActionEvent e) {
		File dirMake = new File(dirSave.getAbsolutePath());
		// 이미지 저장 폴더 생성
		if (!dirMake.exists()) {
			dirMake.mkdir();
		}
		if (photoDelete == true) {
			DBUtil.alertDisplay(1, "등록 실패", "사진이 등록되지 않았습니다", "다시 확인해주세요.");
			return;
		}
		if (idCheck == false) {
			DBUtil.alertDisplay(1, "등록 실패", "아이디 중복체크가 되지 않았습니다.", "아이디 중복체크를 확인해주세요.");
			return;
		}
		// 이미지 파일 저장
		fileName = imageSave(selectedFile);
		try {
			if (txtId.getText().equals("") || txtPassword.getText().equals("") || txtPasswordCheck.getText().equals("")
					|| txtName.getText().equals("") || dpBirth.getValue().toString().equals("")
					|| txtPhoneNumber.getText().equals("") || cbSurvey.getValue().toString().equals("")
					|| fileName == null) {
				DBUtil.alertDisplay(1, "회원가입 오류", "입력되지않은 항목이 있습니다.", "다시 확인해주세요.");
				throw new Exception();
			} else if (!(txtPassword.getText().equals(txtPasswordCheck.getText()))) {
				DBUtil.alertDisplay(1, "회원가입 오류", "비밀번호가 일치하지 않습니다.", "다시 입력해주세요.");
				txtPasswordCheck.clear();
				throw new Exception();

			} else {
				UserVO uvo = new UserVO(txtId.getText(), txtPassword.getText(), txtPasswordCheck.getText(),
						txtName.getText(), dpBirth.getValue().toString(), txtPhoneNumber.getText(),
						cbSurvey.getValue().toString(), fileName);
				// DB를 부르는 명령
				userDAO = new UserDAO();
				// 데이터베이스 테이블에 입력값을 입력하는 함수
				userDAO.getUserRegiste(uvo);
				// 알림창 띄우기
				((Stage) btnSignUp.getScene().getWindow()).close();
				DBUtil.alertDisplay(1, "회원가입", "회원가입 되었습니다.", "즐거운 이용 되세요^0^");
			}
		} catch (Exception e1) {

		}

	}

	// 이미지 저장
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