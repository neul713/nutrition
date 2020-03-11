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
	// 테이블뷰를 선택했을때 위치값과 객체값을 저장할수있는 변수 선언
	private ObservableList<ProductVO> selectedProduct;
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	private File selectedFile = null;
	// TOP3 세팅
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
		// 로그인한 아이디 받아오기
		result();
		// 로그아웃 버튼 눌렀을때
		btnLogOut.setOnAction(e -> {
			handlerBtnLogOutAction(e);
		});
		// 내 추천목록 버튼 눌렀을때
		btnMyRecommend.setOnAction(e -> {
			handlerBtnMyRecommendListAction(e);
		});
		// 제품정보 테이블 뷰 세팅
		searchTableViewSetting();
		productTotalList();
		// 효능으로 검색 버튼 눌렀을때
		btnEfficacySearch.setOnAction(e -> {
			handlerBtnEfficacySearchAction(e);
		});
		// 이름으로 검색 버튼 눌렀을때
		btnNameSearch.setOnAction(e -> {
			handlerBtnNameSearchAction(e);
		});
		// 전체보기 버튼을 눌렀을때
		btnSearchAllView.setOnAction(e -> {
			handlerBtnSearchAllViewAction(e);
		});
		// 마이 페이지 버튼 눌렀을때
		btnMyPage.setOnAction(e -> {
			handlerBtnMyPageAction(e);
		});
		// 테이블 뷰를 클릭했을때 상세정보에 뿌려줌
		searchTableView.setOnMousePressed(e -> {
			handlerTableViewPressedAction(e);
		});
		// 제품 상세정보 텍스트필드 비활성화
		detailsTextFieldSetting();

		// 제품 상세정보에서 추천 버튼을 눌렀을때 일어나는 이벤트!
		btnRecommend.setOnAction(e -> {
			handlerBtnRecommendAction(e);
		});
		// TOP3 세팅
		topThreeSetting();
		// 리프레쉬 버튼 이벤트
		btnRefresh.setOnAction(e -> {
			handlerBtnRefreshAction(e);
		});
		// 관리자에게 메세지보내기 
		btnSendMessage.setOnAction(e -> {
			handlerBtnSendMessageAction(e);
		});
	}

	//로그아웃 버튼 눌렀을때
	public void handlerBtnLogOutAction(ActionEvent e) {

		Parent mainView = null;
		Stage mainStage = null;

		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("로그아웃");
			alert.setHeaderText("로그아웃 성공");
			alert.setContentText("로그아웃 됩니다.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				LoginIdSave.saveId.clear();
				((Stage) btnSearchAllView.getScene().getWindow()).close();
				mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
				Scene scene = new Scene(mainView);
				mainStage = new Stage();
				mainStage.setTitle("로그인");
				mainStage.setScene(scene);
				mainStage.setResizable(true);
				mainStage.show();
				
			} else {

			}

		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "로그아웃 실패", "로그아웃  실패", "로그아웃 하지 못했습니다.");
			e1.printStackTrace();
		}
	}

	// 내 추천목록 버튼 눌렀을때
	public void handlerBtnMyRecommendListAction(ActionEvent e) {
		Parent myRecommendView = null;
		Stage myRecommendStage = null;
		try {
			myRecommendView = FXMLLoader.load(getClass().getResource("/view/myrecommend.fxml"));
			Scene scene = new Scene(myRecommendView);
			myRecommendStage = new Stage();
			myRecommendStage.setTitle("나의 추천 목록");
			myRecommendStage.setScene(scene);
			myRecommendStage.setResizable(false);
			myRecommendStage.show();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// 로그인한 아이디 받아오기
	public void result() {
		lblId.setText(LoginIdSave.saveId.get(0));

	}

	// 제품정보 테이블 뷰 세팅
	public void searchTableViewSetting() {
		TableColumn colProductName = new TableColumn("제품명");
		colProductName.setPrefWidth(249);
		colProductName.setStyle("-fx-alignment: CENTER;");
		colProductName.setCellValueFactory(new PropertyValueFactory("productName"));

		TableColumn colProductPrice = new TableColumn("제품 가격");
		colProductPrice.setPrefWidth(199);
		colProductPrice.setStyle("-fx-alignment: CENTER;");
		colProductPrice.setCellValueFactory(new PropertyValueFactory("productPrice"));

		TableColumn colProductEfficacy = new TableColumn("제품 효능");
		colProductEfficacy.setPrefWidth(350);
		colProductEfficacy.setStyle("-fx-alignment: CENTER;");
		colProductEfficacy.setCellValueFactory(new PropertyValueFactory("productEfficacy"));

		// 테이블설정 컬럼들 객체를 테이블뷰에 리스트추가 및 항목추가
		searchTableView.setItems(productData);
		searchTableView.getColumns().addAll(colProductName, colProductPrice, colProductEfficacy);
	}

	// 제품 정보 테이블뷰에 뿌려주기
	public void productTotalList() {
		productData = FXCollections.observableArrayList();
		ArrayList<ProductVO> list2 = null;
		ProductDAO productDAO = new ProductDAO();
		ProductVO productVO = null;
		list2 = productDAO.getProductTotal();
		if (list2 == null) {
			DBUtil.alertDisplay(1, "경고", "데이터베이스 가져오기 오류", "점검요망");
			return;
		}
		for (int i = 0; i < list2.size(); i++) {
			productData.remove(productData);
			productVO = list2.get(i);
			productData.add(productVO);
		}
		searchTableView.setItems(productData);
	}

	// 효능으로 검색 버튼 눌렀을때
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

	// 이름으로 검색 버튼 눌렀을때
	public void handlerBtnNameSearchAction(ActionEvent e) {
		try {
			ArrayList<ProductVO> list = new ArrayList<ProductVO>();
			ProductDAO productDAO = new ProductDAO();
			list = productDAO.getProductCheck(txtNameSearch.getText());
			if (list == null) {
				throw new Exception("찾으시는 제품이 없습니다.");
			}
			productData.removeAll(productData);
			for (ProductVO pvo : list) {
				productData.add(pvo);
			}
		} catch (Exception e2) {
			e.toString();
		}
	}

	// 전체보기 버튼을 눌렀을때
	public void handlerBtnSearchAllViewAction(ActionEvent e) {
		try {
			productData.removeAll(productData);
			productTotalList();
		} catch (Exception e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "제품 전체보기", "제품 전체보기 오류", "다시 확인해주세요.");
		}
	}

	// 마이 페이지 버튼 눌렀을때
	public void handlerBtnMyPageAction(ActionEvent e) {
		Parent mypageView = null;
		Stage mypageStage = null;
		try {
			mypageView = FXMLLoader.load(getClass().getResource("/view/mypage.fxml"));
			Scene scene = new Scene(mypageView);
			mypageStage = new Stage();
			mypageStage.setTitle("마이페이지");
			mypageStage.setScene(scene);
			mypageStage.setResizable(false);
			mypageStage.show();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// 제품 상세정보 텍스트필드 비활성화
	public void detailsTextFieldSetting() {
		detailsProductName.setEditable(false);
		detailsProductPrice.setEditable(false);
		detailsProductEfficacy.setEditable(false);
		detailsProductCompany.setEditable(false);
		detailsProductIngredient.setEditable(false);
	}

	// 테이블 뷰를 클릭했을때 상세정보 창 띄움
	public void handlerTableViewPressedAction(MouseEvent e) {
		try {
			if (e.getClickCount() != 2) {
				return;
			}
			selectedProduct = searchTableView.getSelectionModel().getSelectedItems();

			String fileName = selectedProduct.get(0).getProductImage();
			selectedFile = new File("C:/productimages/" + fileName);
			if (selectedFile != null) {
				// 이미지 파일 경로
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

	// 제품상세정보에서 추천 버튼을 눌렀을때 일어나는 이벤트!
	public void handlerBtnRecommendAction(ActionEvent e) {
		RecommendDAO rdao = null;
		if (selectedProduct == null) {
			return;
		}
		try {
			rdao = new RecommendDAO();
			int duplicateRecommend = rdao.getDuplicateRecommend(lblId.getText(), detailsProductName.getText());
			if (duplicateRecommend != 0) {
				DBUtil.alertDisplay(1, "제품 추천", "이미 추천된 제품입니다.", "다른 제품도 추천 많이 해주세요^0^~");
				return;
			} else {
				rdao.getRecommendRegiste(lblId.getText(), detailsProductName.getText());
				DBUtil.alertDisplay(5, "제품 추천", "제품이 추천되었습니다.", "다른 제품도 추천 많이 해주세요^0^~");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// TOP3 세팅
	public void topThreeSetting() {
		// 텍스트필드 수정 못하게 함
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
		// 1위
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
		// 2위
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
		// 3위
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

	// 리프레쉬 버튼 이벤트
	public void handlerBtnRefreshAction(ActionEvent e) {
		topThreeSetting();
	}
	// 관리자에게 메세지보내기 
	public void handlerBtnSendMessageAction(ActionEvent e) {
		Parent messageView = null;
		Stage messageStage = null;
		try {
			messageView = FXMLLoader.load(getClass().getResource("/view/message.fxml"));
			Scene scene = new Scene(messageView);
			messageStage = new Stage(StageStyle.UTILITY);			
			messageStage.initModality(Modality.WINDOW_MODAL);
			messageStage.initOwner(btnSendMessage.getScene().getWindow());
			messageStage.setTitle("관리자에게 문의하기");
			
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
					DBUtil.alertDisplay(5, "메세지 보내기", "관리자에게 메세지 보내기", "메세지가 전송되었습니다.");
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
