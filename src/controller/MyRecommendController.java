package controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.LoginIdSave;
import model.ProductVO;

public class MyRecommendController implements Initializable {
	@FXML
	private Label lblId;
	@FXML
	private ImageView imgRecommendProduct;
	@FXML
	private TableView<ProductVO> recommendTableView = new TableView<ProductVO>();
	private ObservableList<ProductVO> productData;
	private ObservableList<ProductVO> selectProduct;
	private File selectedFile = null;
	private String localUrl = ""; // �̹��� ���� ���
	private Image localImage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �α����� ���̵� �޾ƿ���
		result();
		// ���̺� �� ����
		productTableViewSetting();
		// ��ǰ �⺻�̹��� ����
		productImageViewInit();
		// �����͸� ���̺� �ѷ��ֱ�
		productTotalList();
		// ���̺�並 Ŭ��������
		recommendTableView.setOnMousePressed(e -> {
			handlerRecommendTableViewPressedAction(e);
		});

	}

	// �α����� ���̵� �޾ƿ���
	public void result() {
		lblId.setText(LoginIdSave.saveId.get(0));
	}

	// ��ǰ �⺻�̹��� ����
	public void productImageViewInit() {
		localUrl = "/images/vitaminImg.jpg";
		localImage = new Image(localUrl, false);
		imgRecommendProduct.setImage(localImage);
		imgRecommendProduct.setFitHeight(230);
		imgRecommendProduct.setFitWidth(200);
		imgRecommendProduct.setLayoutX(684);
		imgRecommendProduct.setLayoutY(244);
	}

	// ���̺� �� ����
	public void productTableViewSetting() {

		TableColumn colProductName = new TableColumn("��ǰ��");
		colProductName.setPrefWidth(110);
		colProductName.setStyle("-fx-alignment: CENTER;");
		colProductName.setCellValueFactory(new PropertyValueFactory("productName"));

		TableColumn colProductPrice = new TableColumn("��ǰ ����");
		colProductPrice.setPrefWidth(120);
		colProductPrice.setStyle("-fx-alignment: CENTER;");
		colProductPrice.setCellValueFactory(new PropertyValueFactory("productPrice"));

		TableColumn colProductEfficacy = new TableColumn("��ǰ ȿ��");
		colProductEfficacy.setPrefWidth(148);
		colProductEfficacy.setStyle("-fx-alignment: CENTER;");
		colProductEfficacy.setCellValueFactory(new PropertyValueFactory("productEfficacy"));

		TableColumn colProductCompany = new TableColumn("��ǰ ȸ��");
		colProductCompany.setPrefWidth(100);
		colProductCompany.setStyle("-fx-alignment: CENTER;");
		colProductCompany.setCellValueFactory(new PropertyValueFactory("productCompany"));

		TableColumn colProductIngredient = new TableColumn("��ǰ ����");
		colProductIngredient.setPrefWidth(200);
		colProductIngredient.setStyle("-fx-alignment: CENTER;");
		colProductIngredient.setCellValueFactory(new PropertyValueFactory("productIngredient"));

		// ���̺��� �÷��� ��ü�� ���̺�信 ����Ʈ�߰� �� �׸��߰�
		recommendTableView.setItems(productData);
		recommendTableView.getColumns().addAll(colProductName, colProductPrice, colProductEfficacy, colProductCompany,
				colProductIngredient);
	}

	// �����͸� ���̺� �ѷ��ֱ�
	public void productTotalList() {
		productData = FXCollections.observableArrayList();
		ArrayList<ProductVO> list2 = null;
		RecommendDAO recommendDAO = new RecommendDAO();
		ProductVO productVO = null;
		list2 = recommendDAO.getMyRecommend(lblId.getText());
		if (list2 == null) {
			DBUtil.alertDisplay(1, "���", "�����ͺ��̽� �������� ����", "���˿��");
			return;
		}
		for (int i = 0; i < list2.size(); i++) {
			productData.remove(productData);
			productVO = list2.get(i);
			productData.add(productVO);
		}
		recommendTableView.setItems(productData);
	}

	// ���̺�並 Ŭ��������
	public void handlerRecommendTableViewPressedAction(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		selectProduct = recommendTableView.getSelectionModel().getSelectedItems();
		String fileName = selectProduct.get(0).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName);
		if (selectedFile != null) {
			// �̹��� ���� ���/
			try {
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgRecommendProduct.setImage(localImage);
				imgRecommendProduct.setFitHeight(230);
				imgRecommendProduct.setFitWidth(200);
				imgRecommendProduct.setLayoutX(684);
				imgRecommendProduct.setLayoutY(244);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}

}
