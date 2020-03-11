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
	private String localUrl = ""; // �̹��� ���� ���
	private Image localImage;
	// �̹��� ������ ������ �Ű������� ���� ��ü ����
	private File dirSave = new File("C:/userImages");
	// �̹��� �ҷ��� ������ ������ ���� ��ü ����
	private File file = null;
	private String fileName;

	// ���� ���� ��ư ������ ���� ������ ������������� ���� ������ �� �����ϴ� ����..
	private boolean photoDelete = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �α����� ���̵� �޾ƿ���
		result();
		// ������ ���� ���̵� ��й�ȣ Ȯ�������� ����� �� ���� ��.
		myPageSetting();
		// �������� ��������
		textSetting();
		// �� ���� ��������
		btnMyPageCheck.setOnAction(e -> {
			handlerBtnMyPageCheckAction(e);
		});
		// ���� ��� ��ư
		btnUserPhotoUp.setOnAction(e -> {
			handlerBtnUserPhotoUpAction(e);
		});
		// ���� ���� ��ư
		btnUserPhotoDown.setOnAction(e -> {
			handlerBtnImageFileAction(e);
		});
		// ���� �Ϸ� ��ư �̺�Ʈ ó��
		btnEdit.setOnAction(e -> {
			handlerBtnEditAction(e);
		});
		// Ż�� ��ư �̺�Ʈ ó��
		btnUserSecession.setOnAction(e -> {
			handlerBtnUserSecessionAction(e);
		});
	}
	// �α����� ���̵� �޾ƿ���
	public void result() {
		txtIdEntryCheck.setText(LoginIdSave.saveId.get(0));
	}

	// ������ ���� ���̵� ��й�ȣ Ȯ�������� ����� �� ���� ��.
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

	// �������� ��������
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

	// �� ���� ��������
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
						// ���� �α��� Ȯ�� �Ŀ� ������ �� �ִ� �ؽ�Ʈ�ʵ�� ��ư ����� �� �ְ� Ǯ����.
						txtPassword.setDisable(false);
						txtPasswordCheck.setDisable(false);
						txtPhoneNumber.setDisable(false);
						btnUserPhotoUp.setDisable(false);
						btnUserPhotoDown.setDisable(false);
						btnEdit.setDisable(false);
						btnUserSecession.setDisable(false);
						// �α����� �Ϸ�Ǿ����� �� ���� ��������
						ArrayList<UserVO> list2 = null;
						UserDAO uDAO = new UserDAO();
						UserVO userVO = null;

						list2 = uDAO.getUserCheck2(txtIdEntryCheck.getText());
						if (list2 == null) {
							DBUtil.alertDisplay(1, "���", "�����ͺ��̽� �������� ����", "���˿��");
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
							// �̹��� ���� ���
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
						DBUtil.alertDisplay(1, "�α��� ����", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.", "�ٽ� �Է����ּ���.");
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
			DBUtil.alertDisplay(1, "�α��� ����", "�������� �ʴ� ���̵��Դϴ�.", "�ٽ� Ȯ�����ּ���.");
			txtIdEntryCheck.clear();
			txtPasswordEntryCheck.clear();
			return;
		}
	}

	// ���� ��� ��ư
	public void handlerBtnUserPhotoUpAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		try {
			selectedFile = fileChooser.showOpenDialog(btnUserPhotoUp.getScene().getWindow());
			if (selectedFile != null) {
				// �̹��� ���� ���
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

	// ���� ���� ��ư
	public void handlerBtnImageFileAction(ActionEvent e) {
		fileName = imageSave(null);
		imgUserPhoto.setImage(null);
		photoDelete = true;
	}

	// ���� �Ϸ� ��ư �̺�Ʈ ó��
	public void handlerBtnEditAction(ActionEvent e) {
		File dirMake = new File(dirSave.getAbsolutePath());
		// �̹��� ���� ���� ����
		if (!dirMake.exists()) {
			dirMake.mkdir();
		}
		if (photoDelete == true) {
			DBUtil.alertDisplay(1, "��� ����", "������ ��ϵ��� �ʾҽ��ϴ�", "�ٽ� Ȯ�����ּ���.");
			return;
		}
		// �̹��� ���� ����
		fileName = imageSave(selectedFile);
		try {
			if (txtId.getText().equals("") || txtPassword.getText().equals("") || txtPasswordCheck.getText().equals("")
					|| txtName.getText().equals("") || txtBirth.equals("") || txtPhoneNumber.getText().equals("")
					|| txtSurbey.equals("") || fileName == null) {
				DBUtil.alertDisplay(1, "���� ����", "�Է����� ���� �׸��� �ֽ��ϴ�.", "�ٽ� Ȯ�����ּ���.");
				throw new Exception();
			} else if (!(txtPassword.getText().equals(txtPasswordCheck.getText()))) {
				DBUtil.alertDisplay(1, "���� ���� ����", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.", "�ٽ� �Է����ּ���.");
				txtPasswordCheck.clear();
				throw new Exception();
			} else {
				UserVO uvo = new UserVO(txtId.getText(), txtPassword.getText(), txtPasswordCheck.getText(),
						txtName.getText(), txtBirth.getText(), txtPhoneNumber.getText(), txtSurbey.getText(), fileName);
				// DB�� �θ��� ���
				userDAO = new UserDAO();
				// �����ͺ��̽� ���̺� �Է°��� �Է��ϴ� �Լ�
				userDAO.getUserDelete(uvo.getId());
				userDAO.getUserRegiste(uvo);
				// �˸�â  ����
				((Stage) btnEdit.getScene().getWindow()).close();
				DBUtil.alertDisplay(1, "���� ����", "ȸ�� ������ �����Ǿ����ϴ�.", "��ſ� �̿� �Ǽ���^0^");
			}
		} catch (Exception e1) {

		}
	}

	// Ż�� ��ư �̺�Ʈ ó��
	public void handlerBtnUserSecessionAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Ż��");
			alert.setHeaderText("Ż�� Ȯ��");
			alert.setContentText("���� Ż���Ͻðڽ��ϱ�?");
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
			// �̹��� ���ϸ� ����
			fileName = "user" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

			// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
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