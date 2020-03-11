package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class IdPasswordFindController implements Initializable {

	@FXML
	private TextField txtName;
	@FXML
	private DatePicker dpBirth;
	@FXML
	private Button btnIdFind;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtPhone;
	@FXML
	private Button btnPasswordFind;

	private String dateString = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// ���̵� ã�⿡�� �Է��ϰ� �˻���ư.
		btnIdFind.setOnAction(e -> {
			handlerBtnIdFindAction(e);
		});
		// ������� ���ڿ��� ��ȯ
		dpBirth.setOnAction(e -> {
			LocalDate date = dpBirth.getValue();
			dateString = date.toString();
		});
		// ��й�ȣ ã�⿡�� �Է��ϰ� �˻���ư.
		btnPasswordFind.setOnAction(e -> {
			handlerBtnPasswordFindAction(e);
		});

	}

	// ���̵� ã�⿡�� �Է��ϰ� �˻���ư.
	public void handlerBtnIdFindAction(ActionEvent e) {
		String saveId = null;
		UserDAO userDAO = new UserDAO();

		if ((dateString == null) || (txtName.getText().equals(""))) {
			DBUtil.alertDisplay(1, "���� ���Է�", "������ �� �Էµ��� �ʾҽ��ϴ�.", "������ ��� �Է����ּ���.");
		} else {
			saveId = userDAO.getUserIdFind(txtName.getText(), dpBirth.getValue().toString());
			if (saveId == null) {
				DBUtil.alertDisplay(1, "���̵� ã�� ����", "�Է��Ͻ� ������ �´� ���̵� �����ϴ�.", "�ٽ� �Է����ּ���.");
			} else {
				DBUtil.alertDisplay(1, "���̵� ã�� ����", "ã���ô� ���̵�� " + saveId + " �Դϴ�.", "�α��� ���ּ���.");
			}
			dateString = null;
		}

	}

	// ��й�ȣ ã�⿡�� �Է��ϰ� �˻���ư.
	public void handlerBtnPasswordFindAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		String savePassword = null;

		savePassword = userDAO.getUserPasswordFind(txtId.getText(), txtPhone.getText());

		if ((txtId.getText().equals("")) || (txtPhone.getText().equals(""))) {
			DBUtil.alertDisplay(1, "���� ���Է�", "������ �� �Էµ��� �ʾҽ��ϴ�.", "������ ��� �Է����ּ���.");
		} else {
			if (savePassword == null) {
				DBUtil.alertDisplay(1, "��й�ȣ ã�� ����", "�Է��Ͻ� ������ �´� ��й�ȣ�� �����ϴ�.", "�ٽ� �Է����ּ���.");
			} else {
				DBUtil.alertDisplay(1, "��й�ȣ ã�� ����", "ã���ô� ��й�ȣ�� " + savePassword + " �Դϴ�.", "�α��� ���ּ���.");
			}
		}
	}

}