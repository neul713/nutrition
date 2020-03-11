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
		// 아이디 찾기에서 입력하고 검색버튼.
		btnIdFind.setOnAction(e -> {
			handlerBtnIdFindAction(e);
		});
		// 생년월일 문자열로 변환
		dpBirth.setOnAction(e -> {
			LocalDate date = dpBirth.getValue();
			dateString = date.toString();
		});
		// 비밀번호 찾기에서 입력하고 검색버튼.
		btnPasswordFind.setOnAction(e -> {
			handlerBtnPasswordFindAction(e);
		});

	}

	// 아이디 찾기에서 입력하고 검색버튼.
	public void handlerBtnIdFindAction(ActionEvent e) {
		String saveId = null;
		UserDAO userDAO = new UserDAO();

		if ((dateString == null) || (txtName.getText().equals(""))) {
			DBUtil.alertDisplay(1, "정보 미입력", "정보가 다 입력되지 않았습니다.", "정보를 모두 입력해주세요.");
		} else {
			saveId = userDAO.getUserIdFind(txtName.getText(), dpBirth.getValue().toString());
			if (saveId == null) {
				DBUtil.alertDisplay(1, "아이디 찾기 실패", "입력하신 정보에 맞는 아이디가 없습니다.", "다시 입력해주세요.");
			} else {
				DBUtil.alertDisplay(1, "아이디 찾기 성공", "찾으시는 아이디는 " + saveId + " 입니다.", "로그인 해주세요.");
			}
			dateString = null;
		}

	}

	// 비밀번호 찾기에서 입력하고 검색버튼.
	public void handlerBtnPasswordFindAction(ActionEvent e) {
		UserDAO userDAO = new UserDAO();
		String savePassword = null;

		savePassword = userDAO.getUserPasswordFind(txtId.getText(), txtPhone.getText());

		if ((txtId.getText().equals("")) || (txtPhone.getText().equals(""))) {
			DBUtil.alertDisplay(1, "정보 미입력", "정보가 다 입력되지 않았습니다.", "정보를 모두 입력해주세요.");
		} else {
			if (savePassword == null) {
				DBUtil.alertDisplay(1, "비밀번호 찾기 실패", "입력하신 정보에 맞는 비밀번호가 없습니다.", "다시 입력해주세요.");
			} else {
				DBUtil.alertDisplay(1, "비밀번호 찾기 성공", "찾으시는 비밀번호는 " + savePassword + " 입니다.", "로그인 해주세요.");
			}
		}
	}

}