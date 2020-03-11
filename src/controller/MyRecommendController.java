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
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 로그인한 아이디 받아오기
		result();
		// 테이블 뷰 세팅
		productTableViewSetting();
		// 제품 기본이미지 셋팅
		productImageViewInit();
		// 데이터를 테이블에 뿌려주기
		productTotalList();
		// 테이블뷰를 클릭했을때
		recommendTableView.setOnMousePressed(e -> {
			handlerRecommendTableViewPressedAction(e);
		});

	}

	// 로그인한 아이디 받아오기
	public void result() {
		lblId.setText(LoginIdSave.saveId.get(0));
	}

	// 제품 기본이미지 셋팅
	public void productImageViewInit() {
		localUrl = "/images/vitaminImg.jpg";
		localImage = new Image(localUrl, false);
		imgRecommendProduct.setImage(localImage);
		imgRecommendProduct.setFitHeight(230);
		imgRecommendProduct.setFitWidth(200);
		imgRecommendProduct.setLayoutX(684);
		imgRecommendProduct.setLayoutY(244);
	}

	// 테이블 뷰 세팅
	public void productTableViewSetting() {

		TableColumn colProductName = new TableColumn("제품명");
		colProductName.setPrefWidth(110);
		colProductName.setStyle("-fx-alignment: CENTER;");
		colProductName.setCellValueFactory(new PropertyValueFactory("productName"));

		TableColumn colProductPrice = new TableColumn("제품 가격");
		colProductPrice.setPrefWidth(120);
		colProductPrice.setStyle("-fx-alignment: CENTER;");
		colProductPrice.setCellValueFactory(new PropertyValueFactory("productPrice"));

		TableColumn colProductEfficacy = new TableColumn("제품 효능");
		colProductEfficacy.setPrefWidth(148);
		colProductEfficacy.setStyle("-fx-alignment: CENTER;");
		colProductEfficacy.setCellValueFactory(new PropertyValueFactory("productEfficacy"));

		TableColumn colProductCompany = new TableColumn("제품 회사");
		colProductCompany.setPrefWidth(100);
		colProductCompany.setStyle("-fx-alignment: CENTER;");
		colProductCompany.setCellValueFactory(new PropertyValueFactory("productCompany"));

		TableColumn colProductIngredient = new TableColumn("제품 성분");
		colProductIngredient.setPrefWidth(200);
		colProductIngredient.setStyle("-fx-alignment: CENTER;");
		colProductIngredient.setCellValueFactory(new PropertyValueFactory("productIngredient"));

		// 테이블설정 컬럼들 객체를 테이블뷰에 리스트추가 및 항목추가
		recommendTableView.setItems(productData);
		recommendTableView.getColumns().addAll(colProductName, colProductPrice, colProductEfficacy, colProductCompany,
				colProductIngredient);
	}

	// 데이터를 테이블에 뿌려주기
	public void productTotalList() {
		productData = FXCollections.observableArrayList();
		ArrayList<ProductVO> list2 = null;
		RecommendDAO recommendDAO = new RecommendDAO();
		ProductVO productVO = null;
		list2 = recommendDAO.getMyRecommend(lblId.getText());
		if (list2 == null) {
			DBUtil.alertDisplay(1, "경고", "데이터베이스 가져오기 오류", "점검요망");
			return;
		}
		for (int i = 0; i < list2.size(); i++) {
			productData.remove(productData);
			productVO = list2.get(i);
			productData.add(productVO);
		}
		recommendTableView.setItems(productData);
	}

	// 테이블뷰를 클릭했을때
	public void handlerRecommendTableViewPressedAction(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		selectProduct = recommendTableView.getSelectionModel().getSelectedItems();
		String fileName = selectProduct.get(0).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName);
		if (selectedFile != null) {
			// 이미지 파일 경로/
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
