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
	// ���԰�� �޺��ڽ�
	ObservableList<String> survey;

	@FXML
	private TableView<UserVO> tableView = new TableView<>();
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

	// ���� ���� ��ư ������ ������ �����ϲ� ���� ��� ���� �ϴ��� ȸ������ �ȵǰ� �ϴ� ����
	private boolean photoDelete = false;
	// ȸ�����Կ� ���Ǵ� ����..
	private boolean idCheck = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �������� ��������
		textSetting();
		// ���԰�� �޺��ڽ�
		survey = FXCollections.observableArrayList("���ͳ� �˻�", "���� �Ұ�", "��Ʃ��/����", "SNS");
		cbSurvey.setItems(survey);
		// �ߺ�Ȯ�� ��ư �̺�Ʈ ó��
		btnDuplicate.setOnAction(e -> {
			handlerBtnDuplicateAction(e);
		});
		// ���� ��� ��ư
		btnUserPhotoUp.setOnAction(e -> {
			handlerBtnUserPhotoUpAction(e);
		});
		// ���� ���� ��ư
		btnUserPhotoDown.setOnAction(e -> {
			handlerBtnImageFileAction(e);
		});
		// ȸ������ ��ư �̺�Ʈ ó��
		btnSignUp.setOnAction(e -> {
			handlerBtnSignUpAction(e);
		});
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

	// �ߺ�Ȯ�� ��ư �̺�Ʈ ó��
	public void handlerBtnDuplicateAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		int i = userDAO.getUserIdSearch(txtId.getText());
		idCheck = true;
		if (i == 1) {
			DBUtil.alertDisplay(1, "���̵� �ߺ�", "�̹� ������� ���̵��Դϴ�.", "�ٸ� ���̵� �Է����ּ���.");
			btnSignUp.setDisable(true);
			return;
		} else if (i == -1) {
			DBUtil.alertDisplay(1, "���̵� ��밡��", "����� �� �ִ� ���̵��Դϴ�.", "�ٸ� �׸� �Է����ּ���.");
			btnSignUp.setDisable(false);
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
		imgUserPhoto.setLayoutY(42);
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

	// ȸ������ ��ư �̺�Ʈ ó��
	public void handlerBtnSignUpAction(ActionEvent e) {
		File dirMake = new File(dirSave.getAbsolutePath());
		// �̹��� ���� ���� ����
		if (!dirMake.exists()) {
			dirMake.mkdir();
		}
		if (photoDelete == true) {
			DBUtil.alertDisplay(1, "��� ����", "������ ��ϵ��� �ʾҽ��ϴ�", "�ٽ� Ȯ�����ּ���.");
			return;
		}
		if (idCheck == false) {
			DBUtil.alertDisplay(1, "��� ����", "���̵� �ߺ�üũ�� ���� �ʾҽ��ϴ�.", "���̵� �ߺ�üũ�� Ȯ�����ּ���.");
			return;
		}
		// �̹��� ���� ����
		fileName = imageSave(selectedFile);
		try {
			if (txtId.getText().equals("") || txtPassword.getText().equals("") || txtPasswordCheck.getText().equals("")
					|| txtName.getText().equals("") || dpBirth.getValue().toString().equals("")
					|| txtPhoneNumber.getText().equals("") || cbSurvey.getValue().toString().equals("")
					|| fileName == null) {
				DBUtil.alertDisplay(1, "ȸ������ ����", "�Էµ������� �׸��� �ֽ��ϴ�.", "�ٽ� Ȯ�����ּ���.");
				throw new Exception();
			} else if (!(txtPassword.getText().equals(txtPasswordCheck.getText()))) {
				DBUtil.alertDisplay(1, "ȸ������ ����", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.", "�ٽ� �Է����ּ���.");
				txtPasswordCheck.clear();
				throw new Exception();

			} else {
				UserVO uvo = new UserVO(txtId.getText(), txtPassword.getText(), txtPasswordCheck.getText(),
						txtName.getText(), dpBirth.getValue().toString(), txtPhoneNumber.getText(),
						cbSurvey.getValue().toString(), fileName);
				// DB�� �θ��� ���
				userDAO = new UserDAO();
				// �����ͺ��̽� ���̺� �Է°��� �Է��ϴ� �Լ�
				userDAO.getUserRegiste(uvo);
				// �˸�â ����
				((Stage) btnSignUp.getScene().getWindow()).close();
				DBUtil.alertDisplay(1, "ȸ������", "ȸ������ �Ǿ����ϴ�.", "��ſ� �̿� �Ǽ���^0^");
			}
		} catch (Exception e1) {

		}

	}

	// �̹��� ����
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