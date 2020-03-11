package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.LoginIdSave;
import model.ProductVO;
import model.RecommendVO;

public class MainViewController implements Initializable {
	@FXML
	private Label lblId;
	@FXML
	private Button btnMyRecommend;
	@FXML
	private Button btnRecommend;
	@FXML
	private ToggleGroup SearchGroup;
	@FXML
	private RadioButton searchRadioBtnLiver;
	@FXML
	private RadioButton searchRadioBtnTired;
	@FXML
	private RadioButton searchRadioBtnAntioxidant;
	@FXML
	private RadioButton searchRadioBtnEye;
	@FXML
	private RadioButton searchRadioBtnJoint;
	@FXML
	private RadioButton searchRadioBtnImmunity;
	@FXML
	private Button btnEfficacySearch;
	@FXML
	private TextField txtNameSearch;
	@FXML
	private Button btnNameSearch;
	@FXML
	private Button btnSearchAllView;
	@FXML
	private Button btnMyPage;
	@FXML
	private ImageView detailsProductImage;
	@FXML
	private TextField detailsProductName;
	@FXML
	private TextField detailsProductPrice;
	@FXML
	private TextField detailsProductEfficacy;
	@FXML
	private TextField detailsProductCompany;
	@FXML
	private TextArea detailsProductIngredient;
	@FXML
	private TableView<ProductVO> searchTableView = new TableView<>();
	ObservableList<ProductVO> productData;
	// ���̺�並 ���������� ��ġ���� ��ü���� �����Ҽ��ִ� ���� ����
	private ObservableList<ProductVO> selectedProduct;
	private String localUrl = ""; // �̹��� ���� ���
	private Image localImage;
	private File selectedFile = null;
	// TOP3 ����
	@FXML
	private ImageView imgTop1;
	@FXML
	private ImageView imgTop2;
	@FXML
	private ImageView imgTop3;
	@FXML
	private Label recommendCount1;
	@FXML
	private Label recommendCount2;
	@FXML
	private Label recommendCount3;
	@FXML
	private Label top1Name;
	@FXML
	private Label top2Name;
	@FXML
	private Label top3Name;
	@FXML
	private Label top1Efficacy;
	@FXML
	private Label top2Efficacy;
	@FXML
	private Label top3Efficacy;
	@FXML
	private Button btnRefresh;
	@FXML
	private Button btnLogOut;
	@FXML private Button btnSendMessage;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �α����� ���̵� �޾ƿ���
		result();
		// �α׾ƿ� ��ư ��������
		btnLogOut.setOnAction(e -> {
			handlerBtnLogOutAction(e);
		});
		// �� ��õ��� ��ư ��������
		btnMyRecommend.setOnAction(e -> {
			handlerBtnMyRecommendListAction(e);
		});
		// ��ǰ���� ���̺� �� ����
		searchTableViewSetting();
		productTotalList();
		// ȿ������ �˻� ��ư ��������
		btnEfficacySearch.setOnAction(e -> {
			handlerBtnEfficacySearchAction(e);
		});
		// �̸����� �˻� ��ư ��������
		btnNameSearch.setOnAction(e -> {
			handlerBtnNameSearchAction(e);
		});
		// ��ü���� ��ư�� ��������
		btnSearchAllView.setOnAction(e -> {
			handlerBtnSearchAllViewAction(e);
		});
		// ���� ������ ��ư ��������
		btnMyPage.setOnAction(e -> {
			handlerBtnMyPageAction(e);
		});
		// ���̺� �並 Ŭ�������� �������� �ѷ���
		searchTableView.setOnMousePressed(e -> {
			handlerTableViewPressedAction(e);
		});
		// ��ǰ ������ �ؽ�Ʈ�ʵ� ��Ȱ��ȭ
		detailsTextFieldSetting();

		// ��ǰ ���������� ��õ ��ư�� �������� �Ͼ�� �̺�Ʈ!
		btnRecommend.setOnAction(e -> {
			handlerBtnRecommendAction(e);
		});
		// TOP3 ����
		topThreeSetting();
		// �������� ��ư �̺�Ʈ
		btnRefresh.setOnAction(e -> {
			handlerBtnRefreshAction(e);
		});
		// �����ڿ��� �޼��������� 
		btnSendMessage.setOnAction(e -> {
			handlerBtnSendMessageAction(e);
		});
	}

	//�α׾ƿ� ��ư ��������
	public void handlerBtnLogOutAction(ActionEvent e) {

		Parent mainView = null;
		Stage mainStage = null;

		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("�α׾ƿ�");
			alert.setHeaderText("�α׾ƿ� ����");
			alert.setContentText("�α׾ƿ� �˴ϴ�.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				LoginIdSave.saveId.clear();
				((Stage) btnSearchAllView.getScene().getWindow()).close();
				mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
				Scene scene = new Scene(mainView);
				mainStage = new Stage();
				mainStage.setTitle("�α���");
				mainStage.setScene(scene);
				mainStage.setResizable(true);
				mainStage.show();
				
			} else {

			}

		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "�α׾ƿ� ����", "�α׾ƿ�  ����", "�α׾ƿ� ���� ���߽��ϴ�.");
			e1.printStackTrace();
		}
	}

	// �� ��õ��� ��ư ��������
	public void handlerBtnMyRecommendListAction(ActionEvent e) {
		Parent myRecommendView = null;
		Stage myRecommendStage = null;
		try {
			myRecommendView = FXMLLoader.load(getClass().getResource("/view/myrecommend.fxml"));
			Scene scene = new Scene(myRecommendView);
			myRecommendStage = new Stage();
			myRecommendStage.setTitle("���� ��õ ���");
			myRecommendStage.setScene(scene);
			myRecommendStage.setResizable(false);
			myRecommendStage.show();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// �α����� ���̵� �޾ƿ���
	public void result() {
		lblId.setText(LoginIdSave.saveId.get(0));

	}

	// ��ǰ���� ���̺� �� ����
	public void searchTableViewSetting() {
		TableColumn colProductName = new TableColumn("��ǰ��");
		colProductName.setPrefWidth(249);
		colProductName.setStyle("-fx-alignment: CENTER;");
		colProductName.setCellValueFactory(new PropertyValueFactory("productName"));

		TableColumn colProductPrice = new TableColumn("��ǰ ����");
		colProductPrice.setPrefWidth(199);
		colProductPrice.setStyle("-fx-alignment: CENTER;");
		colProductPrice.setCellValueFactory(new PropertyValueFactory("productPrice"));

		TableColumn colProductEfficacy = new TableColumn("��ǰ ȿ��");
		colProductEfficacy.setPrefWidth(350);
		colProductEfficacy.setStyle("-fx-alignment: CENTER;");
		colProductEfficacy.setCellValueFactory(new PropertyValueFactory("productEfficacy"));

		// ���̺��� �÷��� ��ü�� ���̺�信 ����Ʈ�߰� �� �׸��߰�
		searchTableView.setItems(productData);
		searchTableView.getColumns().addAll(colProductName, colProductPrice, colProductEfficacy);
	}

	// ��ǰ ���� ���̺�信 �ѷ��ֱ�
	public void productTotalList() {
		productData = FXCollections.observableArrayList();
		ArrayList<ProductVO> list2 = null;
		ProductDAO productDAO = new ProductDAO();
		ProductVO productVO = null;
		list2 = productDAO.getProductTotal();
		if (list2 == null) {
			DBUtil.alertDisplay(1, "���", "�����ͺ��̽� �������� ����", "���˿��");
			return;
		}
		for (int i = 0; i < list2.size(); i++) {
			productData.remove(productData);
			productVO = list2.get(i);
			productData.add(productVO);
		}
		searchTableView.setItems(productData);
	}

	// ȿ������ �˻� ��ư ��������
	public void handlerBtnEfficacySearchAction(ActionEvent e) {
		String efficacy = "";
		if (searchRadioBtnLiver.isSelected()) {
			efficacy = searchRadioBtnLiver.getText();
		} else if (searchRadioBtnTired.isSelected()) {
			efficacy = searchRadioBtnTired.getText();
		} else if (searchRadioBtnAntioxidant.isSelected()) {
			efficacy = searchRadioBtnAntioxidant.getText();
		} else if (searchRadioBtnEye.isSelected()) {
			efficacy = searchRadioBtnEye.getText();
		} else if (searchRadioBtnJoint.isSelected()) {
			efficacy = searchRadioBtnJoint.getText();
		} else if (searchRadioBtnImmunity.isSelected()) {
			efficacy = searchRadioBtnImmunity.getText();
		}
		try {
			ArrayList<ProductVO> list = new ArrayList<ProductVO>();
			ProductDAO productDAO = new ProductDAO();
			list = productDAO.getProductEfficacyCheck(efficacy);
			if (list == null) {
				throw new Exception();
			}
			productData.removeAll(productData);
			for (ProductVO pvo : list) {
				productData.add(pvo);
			}
		} catch (Exception e2) {
			e.toString();
		}
	}

	// �̸����� �˻� ��ư ��������
	public void handlerBtnNameSearchAction(ActionEvent e) {
		try {
			ArrayList<ProductVO> list = new ArrayList<ProductVO>();
			ProductDAO productDAO = new ProductDAO();
			list = productDAO.getProductCheck(txtNameSearch.getText());
			if (list == null) {
				throw new Exception("ã���ô� ��ǰ�� �����ϴ�.");
			}
			productData.removeAll(productData);
			for (ProductVO pvo : list) {
				productData.add(pvo);
			}
		} catch (Exception e2) {
			e.toString();
		}
	}

	// ��ü���� ��ư�� ��������
	public void handlerBtnSearchAllViewAction(ActionEvent e) {
		try {
			productData.removeAll(productData);
			productTotalList();
		} catch (Exception e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "��ǰ ��ü����", "��ǰ ��ü���� ����", "�ٽ� Ȯ�����ּ���.");
		}
	}

	// ���� ������ ��ư ��������
	public void handlerBtnMyPageAction(ActionEvent e) {
		Parent mypageView = null;
		Stage mypageStage = null;
		try {
			mypageView = FXMLLoader.load(getClass().getResource("/view/mypage.fxml"));
			Scene scene = new Scene(mypageView);
			mypageStage = new Stage();
			mypageStage.setTitle("����������");
			mypageStage.setScene(scene);
			mypageStage.setResizable(false);
			mypageStage.show();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// ��ǰ ������ �ؽ�Ʈ�ʵ� ��Ȱ��ȭ
	public void detailsTextFieldSetting() {
		detailsProductName.setEditable(false);
		detailsProductPrice.setEditable(false);
		detailsProductEfficacy.setEditable(false);
		detailsProductCompany.setEditable(false);
		detailsProductIngredient.setEditable(false);
	}

	// ���̺� �並 Ŭ�������� ������ â ���
	public void handlerTableViewPressedAction(MouseEvent e) {
		try {
			if (e.getClickCount() != 2) {
				return;
			}
			selectedProduct = searchTableView.getSelectionModel().getSelectedItems();

			String fileName = selectedProduct.get(0).getProductImage();
			selectedFile = new File("C:/productimages/" + fileName);
			if (selectedFile != null) {
				// �̹��� ���� ���
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				detailsProductImage.setImage(localImage);
				detailsProductImage.setFitHeight(206);
				detailsProductImage.setFitWidth(300);
				detailsProductImage.setLayoutX(883);
				detailsProductImage.setLayoutY(80);
			}
			detailsProductName.setText(selectedProduct.get(0).getProductName());
			detailsProductPrice.setText(selectedProduct.get(0).getProductPrice());
			detailsProductEfficacy.setText(selectedProduct.get(0).getProductEfficacy());
			detailsProductCompany.setText(selectedProduct.get(0).getProductCompany());
			detailsProductIngredient.setText(selectedProduct.get(0).getProductIngredient());
			detailsProductIngredient.setWrapText(true);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// ��ǰ���������� ��õ ��ư�� �������� �Ͼ�� �̺�Ʈ!
	public void handlerBtnRecommendAction(ActionEvent e) {
		RecommendDAO rdao = null;
		if (selectedProduct == null) {
			return;
		}
		try {
			rdao = new RecommendDAO();
			int duplicateRecommend = rdao.getDuplicateRecommend(lblId.getText(), detailsProductName.getText());
			if (duplicateRecommend != 0) {
				DBUtil.alertDisplay(1, "��ǰ ��õ", "�̹� ��õ�� ��ǰ�Դϴ�.", "�ٸ� ��ǰ�� ��õ ���� ���ּ���^0^~");
				return;
			} else {
				rdao.getRecommendRegiste(lblId.getText(), detailsProductName.getText());
				DBUtil.alertDisplay(5, "��ǰ ��õ", "��ǰ�� ��õ�Ǿ����ϴ�.", "�ٸ� ��ǰ�� ��õ ���� ���ּ���^0^~");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// TOP3 ����
	public void topThreeSetting() {
		// �ؽ�Ʈ�ʵ� ���� ���ϰ� ��
		recommendCount1.setDisable(false);
		recommendCount2.setDisable(false);
		recommendCount3.setDisable(false);
		top1Name.setDisable(false);
		top2Name.setDisable(false);
		top3Name.setDisable(false);
		top1Efficacy.setDisable(false);
		top2Efficacy.setDisable(false);
		top3Efficacy.setDisable(false);
		// =====================================
		RecommendDAO rdao = new RecommendDAO();
		ArrayList<RecommendVO> list = new ArrayList<RecommendVO>();
		list = rdao.getRecommendResult();
		// 1��
		recommendCount1.setText(String.valueOf(list.get(0).getRecommendCount()));
		top1Name.setText(list.get(0).getProductName());
		top1Efficacy.setText(list.get(0).getProductEfficacy());
		String fileName = list.get(0).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName);
		if (selectedFile != null) {
			try {
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgTop1.setImage(localImage);
				imgTop1.setFitWidth(240);
				imgTop1.setFitHeight(150);
				imgTop1.setLayoutX(30);
				imgTop1.setLayoutY(625);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		// 2��
		recommendCount2.setText(String.valueOf(list.get(1).getRecommendCount()));
		top2Name.setText(list.get(1).getProductName());
		top2Efficacy.setText(list.get(1).getProductEfficacy());
		String fileName2 = list.get(1).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName2);
		if (selectedFile != null) {
			try {
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgTop2.setImage(localImage);
				imgTop2.setFitWidth(240);
				imgTop2.setFitHeight(150);
				imgTop2.setLayoutX(307);
				imgTop2.setLayoutY(625);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		// 3��
		recommendCount3.setText(String.valueOf(list.get(2).getRecommendCount()));
		top3Name.setText(list.get(2).getProductName());
		top3Efficacy.setText(list.get(2).getProductEfficacy());
		String fileName3 = list.get(2).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName3);
		if (selectedFile != null) {
			try {
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgTop3.setImage(localImage);
				imgTop3.setFitWidth(240);
				imgTop3.setFitHeight(150);
				imgTop3.setLayoutX(584);
				imgTop3.setLayoutY(625);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	// �������� ��ư �̺�Ʈ
	public void handlerBtnRefreshAction(ActionEvent e) {
		topThreeSetting();
	}
	// �����ڿ��� �޼��������� 
	public void handlerBtnSendMessageAction(ActionEvent e) {
		Parent messageView = null;
		Stage messageStage = null;
		try {
			messageView = FXMLLoader.load(getClass().getResource("/view/message.fxml"));
			Scene scene = new Scene(messageView);
			messageStage = new Stage(StageStyle.UTILITY);			
			messageStage.initModality(Modality.WINDOW_MODAL);
			messageStage.initOwner(btnSendMessage.getScene().getWindow());
			messageStage.setTitle("�����ڿ��� �����ϱ�");
			
			TextField txtMessageId = (TextField) messageView.lookup("#messageId");
			TextArea txtMessage = (TextArea) messageView.lookup("#messeage");
			Button buttonSend = (Button) messageView.lookup("#btnSend");		
			
			txtMessageId.setText(lblId.getText());
			txtMessageId.setEditable(false);
			txtMessage.setWrapText(true);
			buttonSend.setOnAction(e2 -> {
				MessageDAO mdao = null;
				try {
					mdao=new MessageDAO();
					mdao.getSendMessage(txtMessageId.getText(),txtMessage.getText());
					DBUtil.alertDisplay(5, "�޼��� ������", "�����ڿ��� �޼��� ������", "�޼����� ���۵Ǿ����ϴ�.");
					((Stage) buttonSend.getScene().getWindow()).close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			messageStage.setScene(scene);
			messageStage.setResizable(false);
			messageStage.show();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
