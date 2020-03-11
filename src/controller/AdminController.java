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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import model.MessageVO;
import model.PieChartVO;
import model.ProductVO;
import model.UserVO;

public class AdminController implements Initializable {
	// 회원관리 탭
	@FXML
	private TextField txtUserSearch;
	@FXML
	private Button btnUserSearch;
	@FXML
	private Button btnUserDelete;
	@FXML
	private Button btnUserAllView;
	@FXML
	private Button btnChartView;
	@FXML
	private ImageView imgUser;
	@FXML
	private TableView<UserVO> userTableView = new TableView<>();
	ObservableList<UserVO> data;
	private ObservableList<UserVO> selectUser;
	private File selectedFile = null;
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	// 제품관리 탭
	@FXML 
	private TextField txtProductSearch;
	@FXML
	private Button btnProductRegisterOpen;
	@FXML
	private Button btnProductSearch;
	@FXML
	private Button btnProductDelete;
	@FXML
	private Button btnProductAllView;
	@FXML
	private Button btnProductEdit;
	@FXML
	private ImageView imgProduct;
	@FXML
	private TableView<ProductVO> productTableView = new TableView<>();
	private ObservableList<ProductVO> productData;
	private ProductDAO productDAO;
	private ObservableList<ProductVO> selectProduct;
	private int selectedIndex;

	// 제품 이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("c:/productImages");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;
	private String fileName;
	// 메세지 탭
	@FXML
	private TableView<MessageVO> messageTableView = new TableView<>();
	@FXML
	private Button btnMessageDelete;
	private ObservableList<MessageVO> MessageData;
	private ObservableList<MessageVO> selectMessage;
	// 사진 삭제 버튼을 누르고 새로 사진을 등록하지 않으면 제품등록 되지않게 하는 변수
	private boolean photoDelete = false;
	// 제품 이름 중복 체크 변수
	private boolean productCheck = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 1번째 탭 '회원관리' 이벤트 등록
		// ====================================
		// 회원관리 테이블 뷰 세팅
		tableViewSetting();
		// 회원 데이터 테이블 뷰에 뿌려주기
		totalList();
		// 유저 기본 이미지 셋팅
		userImageViewInit();
		// 검색 버튼을 눌렀을때
		btnUserSearch.setOnAction(e -> {
			handlerBtnSearchAction(e);
		});
		// 삭제 버튼을 눌렀을때
		btnUserDelete.setOnAction(e -> {
			handlerBtnDeleteAction(e);
		});
		// 전체보기 버튼을 눌렀을때
		btnUserAllView.setOnAction(e -> {
			handlerBtnAllViewAction(e);
		});
		// 차트보기 버튼을 눌렀을때
		btnChartView.setOnAction(e -> {
			handlerBtnChartViewAction(e);
		});
		// 테이블 뷰를 눌렀을때
		userTableView.setOnMousePressed(e -> {
			handlerUserTableViewPressedAction(e);
		});
		// 2번째 탭 '제품관리' 이벤트 등록
		// ====================================
		// 제품관리 테이블뷰 세팅
		productTableViewSetting();
		// 제품 데이터 테이블 뷰에 뿌려주기
		productTotalList();
		// 제품 기본 이미지 설정
		productImageViewInit();
		// 검색 버튼을 눌렀을때
		btnProductSearch.setOnAction(e -> {
			handlerBtnProductSearchAction(e);
		});
		// 삭제 버튼을 눌렀을때
		btnProductDelete.setOnAction(e -> {
			handlerBtnProductDeleteAction(e);
		});
		// 전체보기 버튼을 눌렀을때
		btnProductAllView.setOnAction(e -> {
			handlerBtnProductAllViewAction(e);
		});
		// 제품등록 버튼을 눌렀을때
		btnProductRegisterOpen.setOnAction(e -> {
			handlerBtnProductRegisterOpenAction(e);
		});
		// 수정 버튼을 눌렀을때
		btnProductEdit.setOnAction(e -> {
			handlerBtnProductEditAction(e);
		});
		// 테이블뷰를 눌렀을때
		productTableView.setOnMousePressed(e -> {
			handlerProductTableViewPressedAction(e);
		});
		// 3번째 탭 '메세지관리' 이벤트
		messageTableViewSetting();
		// 메세지 삭제 버튼
		btnMessageDelete.setOnAction(e -> {
			handlerBtnMessageDeleteAction(e);
		});
		//메세지 데이터 테이블뷰에 뿌려주기
		messageTotalList();
	}

	// 1번째 탭 '회원관리' 함수 등록
	// ======================================================
	// 회원 기본이미지 셋팅
	public void userImageViewInit() {
		localUrl = "/images/defaultPhoto.png";
		localImage = new Image(localUrl, false);
		imgUser.setImage(localImage);
		imgUser.setFitHeight(250);
		imgUser.setFitWidth(200);
	}

	// 회원정보 테이블 뷰 세팅
	public void tableViewSetting() {

		TableColumn colId = new TableColumn("아이디");
		colId.setPrefWidth(100);
		colId.setStyle("-fx-alignment: CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("id"));

		TableColumn colName = new TableColumn("이름");
		colName.setPrefWidth(100);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colBirth = new TableColumn("생년월일");
		colBirth.setPrefWidth(158);
		colBirth.setStyle("-fx-alignment: CENTER;");
		colBirth.setCellValueFactory(new PropertyValueFactory("birth"));

		TableColumn colPhoneNumber = new TableColumn("전화번호");
		colPhoneNumber.setPrefWidth(200);
		colPhoneNumber.setStyle("-fx-alignment: CENTER;");
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));

		TableColumn colSurvey = new TableColumn("유입경로");
		colSurvey.setPrefWidth(120);
		colSurvey.setStyle("-fx-alignment: CENTER;");
		colSurvey.setCellValueFactory(new PropertyValueFactory("survey"));

		userTableView.setItems(data);
		userTableView.getColumns().addAll(colId, colName, colBirth, colPhoneNumber, colSurvey);
	}

	// 회원정보 검색 버튼을 눌렀을때
	public void handlerBtnSearchAction(ActionEvent e) {
		try {
			ArrayList<UserVO> list = new ArrayList<UserVO>();
			UserDAO userDAO = new UserDAO();
			list = userDAO.getUserCheck(txtUserSearch.getText());
			if (list == null) {
				throw new Exception("찾으시는 회원이 없습니다.");
			}
			data.removeAll(data);
			for (UserVO svo : list) {
				data.add(svo);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// 회원정보 삭제 버튼을 눌렀을때
	public void handlerBtnDeleteAction(ActionEvent e) {
		try {
			selectUser = userTableView.getSelectionModel().getSelectedItems();
			UserDAO userDAO = new UserDAO();
			userDAO.getUserDelete(selectUser.get(0).getId());
			data.removeAll(data);
			totalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "회원 삭제", "회원 삭제 오류", "다시 확인해주세요.");
		}
	}

	// 회원정보 전체보기 버튼을 눌렀을때
	public void handlerBtnAllViewAction(ActionEvent e) {
		try {
			data.removeAll(data);
			totalList();
			userImageViewInit();
		} catch (Exception e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "회원 전체보기", "회원 전체보기 오류", "다시 확인해주세요.");
		}
	}

	// 회원정보 차트보기 버튼을 눌렀을때
	public void handlerBtnChartViewAction(ActionEvent e) {
		try {
			Parent pieChartRoot = FXMLLoader.load(getClass().getResource("/view/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnChartView.getScene().getWindow());
			stage.setTitle("사용자의 유입경로 ");

			PieChart pieChart = (PieChart) pieChartRoot.lookup("#pieChart");

			ArrayList<PieChartVO> list = null;
			UserDAO userDAO = new UserDAO();
			list = userDAO.getSurveyResult();

			if (list == null) {
				DBUtil.alertDisplay(1, "경고", "데이터베이스 파이차트 가져오기 오류", "다시 확인해주세요.");
				return;
			}

			ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				pieData.add(new PieChart.Data(list.get(i).getSurvey(), list.get(i).getCount()));
			}
			pieChart.setData(pieData);
			// 그래프 그리기
			Scene scene = new Scene(pieChartRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	// 회원 관리 테이블 뷰를 눌렀을때
	public void handlerUserTableViewPressedAction(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		selectUser = userTableView.getSelectionModel().getSelectedItems();
		String fileName = selectUser.get(0).getUserImage();
		selectedFile = new File("C:/userImages/" + fileName);
		if (selectedFile != null) {
			// 이미지 파일 경로/
			try {
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgUser.setImage(localImage);
				imgUser.setFitHeight(250);
				imgUser.setFitWidth(200);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}

	// 회원 정보 테이블뷰에 뿌려주기
	public void totalList() {
		data = FXCollections.observableArrayList();
		ArrayList<UserVO> list = null;
		UserDAO userDAO = new UserDAO();
		UserVO userVO = null;
		list = userDAO.getUserTotal();

		if (list == null) {
			DBUtil.alertDisplay(1, "경고", "데이터베이스 가져오기 오류", "다시 확인해주세요.");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			userVO = list.get(i);
			data.add(userVO);
		}
		userTableView.setItems(data);
	}

	// 2번째 '제품관리' 함수등록
	// ===================================================================
	// 제품 기본이미지 셋팅
	public void productImageViewInit() {
		localUrl = "/images/vitaminImg.jpg";
		localImage = new Image(localUrl, false);
		imgProduct.setImage(localImage);
		imgProduct.setFitHeight(250);
		imgProduct.setFitWidth(200);
	}

	// 제품정보 테이블 뷰 세팅
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
		productTableView.setItems(productData);
		productTableView.getColumns().addAll(colProductName, colProductPrice, colProductEfficacy, colProductCompany,
				colProductIngredient);
	}

	// 제품정보 전체보기 버튼을 눌렀을때
	public void handlerBtnProductAllViewAction(ActionEvent e) {
		try {
			productData.removeAll(productData);
			productTotalList();
			productImageViewInit();
		} catch (Exception e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "제품 전체보기", "제품 전체보기 오류", "다시 확인해주세요.");
		}
	}

	// 제품정보 삭제 버튼을 눌렀을때
	public void handlerBtnProductDeleteAction(ActionEvent e) {
		try {
			selectProduct = productTableView.getSelectionModel().getSelectedItems();
			ProductDAO productDAO = new ProductDAO();
			productDAO.getProductDelete(selectProduct.get(0).getProductName());
			productData.removeAll(productData);
			productTotalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "제품 삭제", "제품 삭제 오류", "다시 확인해주세요.");
		}

	}

	// 제품정보 검색 버튼을 눌렀을때
	public void handlerBtnProductSearchAction(ActionEvent e) {
		try {
			ArrayList<ProductVO> list2 = new ArrayList<ProductVO>();
			ProductDAO productDAO = new ProductDAO();
			list2 = productDAO.getProductCheck(txtProductSearch.getText());
			if (list2 == null) {
				throw new Exception("찾으시는 제품이 없습니다.");
			}
			productData.removeAll(productData);
			for (ProductVO pvo : list2) {
				productData.add(pvo);
			}
		} catch (Exception e2) {
			e.toString();
		}
	}

	// 제품등록 버튼을 눌렀을때
	public void handlerBtnProductRegisterOpenAction(ActionEvent e) {
		Parent ProductRegisterView = null;
		Stage ProductRegisterStage = null;
		try {
			ProductRegisterView = FXMLLoader.load(getClass().getResource("/view/ProductRegister.fxml"));
			ProductRegisterStage = new Stage(StageStyle.UTILITY);
			ProductRegisterStage.initModality(Modality.WINDOW_MODAL);
			ProductRegisterStage.initOwner(btnProductRegisterOpen.getScene().getWindow());
			ProductRegisterStage.setTitle("제품 등록");

			TextField textProductName = (TextField) ProductRegisterView.lookup("#txtProductName");
			TextField textProductPrice = (TextField) ProductRegisterView.lookup("#txtProductPrice");
			TextField textProductCompany = (TextField) ProductRegisterView.lookup("#txtProductCompany");
			TextArea textProductIngredient = (TextArea) ProductRegisterView.lookup("#txtProductIngredient");
			ComboBox<String> comboBoxEfficacy = (ComboBox<String>) ProductRegisterView.lookup("#cbEfficacy");
			ImageView imageProductPhoto = (ImageView) ProductRegisterView.lookup("#imgProductPhoto");
			Button buttonProductNameCheck = (Button) ProductRegisterView.lookup("#btnProductNameCheck");
			Button buttonProductPhotoRegister = (Button) ProductRegisterView.lookup("#btnProductPhotoRegister");
			Button buttonProductPhotoDelete = (Button) ProductRegisterView.lookup("#btnProductPhotoDelete");
			Button buttonProductRegister = (Button) ProductRegisterView.lookup("#btnProductRegister");
			// 효능 콤보박스 보기 세팅
			ObservableList<String> efficacy = FXCollections.observableArrayList("간 기능 강화", "피로 회복", "항산화 기능 강화",
					"면역력 증가", "관절 영양", "눈 건강");
			comboBoxEfficacy.setItems(efficacy);

			textProductIngredient.setWrapText(true);
			DecimalFormat format = new DecimalFormat("###########");
			// 점수 입력시 길이 제한 이벤트 처리
			textProductPrice.setTextFormatter(new TextFormatter<>(event -> {
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

			productData = FXCollections.observableArrayList();

			// 사진 등록 버튼
			buttonProductPhotoRegister.setOnAction(e1 -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(buttonProductPhotoRegister.getScene().getWindow());
					if (selectedFile != null) {
						// 이미지 파일 경로
						localUrl = selectedFile.toURI().toURL().toString();
					}
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				}
				localImage = new Image(localUrl, false);
				imageProductPhoto.setImage(localImage);
				imageProductPhoto.setLayoutX(381);
				imageProductPhoto.setLayoutY(42);
				imageProductPhoto.setFitHeight(220);
				imageProductPhoto.setFitWidth(193);
				imageProductPhoto.setDisable(false);
				photoDelete = false;
			});
			// 사진 삭제 버튼
			buttonProductPhotoDelete.setOnAction(e1 -> {
				fileName = imageSave(null);
				imageProductPhoto.setImage(null);
				photoDelete = true;
			});
			// 제품 이름 중복 체크 버튼
			buttonProductNameCheck.setOnAction(e1 -> {
				ProductDAO productDAO = new ProductDAO();
				int i = productDAO.getProductNameSearch(textProductName.getText());

				if (i == 1) {
					DBUtil.alertDisplay(1, "제품명 중복", "이미 등록된 제품명입니다.", "다시 확인해주세요.");
					buttonProductRegister.setDisable(true);
					return;

				} else if (i == -1) {
					DBUtil.alertDisplay(1, "제품명 등록 가능", "등록할 수 있는 제품명입니다.", "다른 항목도 입력해주세요.");
					buttonProductRegister.setDisable(false);
					productCheck = true;
					return;

				}
			});
			// 제품 등록 버튼
			buttonProductRegister.setOnAction(e1 -> {
				// 이미지 파일 저장
				String fileName = imageSave(selectedFile);
				File dirMake = new File(dirSave.getAbsolutePath());
				// 이미지 저장 폴더 생성
				if (!dirMake.exists()) {
					dirMake.mkdir();
				}
				if (photoDelete == true) {
					DBUtil.alertDisplay(1, "등록실패", "사진이 등록되지 않았습니다", "다시 확인해주세요.");
					return;
				}
				if (productCheck == false) {
					DBUtil.alertDisplay(1, "등록 실패", "제품명 중복체크가 되지 않았습니다.", "제품명 중복체크를 확인해주세요.");
					return;
				}
				try {
					if (textProductName.getText().equals("") || textProductPrice.getText().equals("")
							|| comboBoxEfficacy.getValue().toString().equals("")
							|| textProductCompany.getText().equals("") || textProductIngredient.getText().equals("")
							|| fileName == null) {
						throw new Exception("왜 안돼");
					} else {
						ProductVO pvo = new ProductVO(textProductName.getText(), textProductPrice.getText(),
								comboBoxEfficacy.getValue().toString(), textProductCompany.getText(),
								textProductIngredient.getText(), fileName);
						// DB를 부르는 명령
						productDAO = new ProductDAO();
						// 데이터베이스 테이블에 입력값을 입력하는 함수
						productDAO.getProductRegiste(pvo);

						if (pvo != null) {

							productTableView.setItems(productData);
							// 알림창 띄우기
							((Stage) buttonProductRegister.getScene().getWindow()).close();
							DBUtil.alertDisplay(1, "등록성공", "테이블뷰에 등록성공", "등록되었습니다.");
							productTotalList();
						} else {
							DBUtil.alertDisplay(1, "등록실패", "테이블뷰에 등록실패", "다시 시도해주세요.");
						}
					}

				} catch (Exception e2) {
					DBUtil.alertDisplay(1, "등록실패", "제품 정보 모두 기재요망", e2.toString());
				}

			});

			Scene scene = new Scene(ProductRegisterView);
			ProductRegisterStage.setScene(scene);
			ProductRegisterStage.setResizable(true);
			ProductRegisterStage.show();

		} catch (IOException e2) {
			DBUtil.alertDisplay(1, "제품 등록창 부르기 실패", "제품 등록창 부르기 실패", e2.toString());
		}
	}

	// 수정 버튼을 눌렀을때
	public void handlerBtnProductEditAction(ActionEvent e) {
		if (selectProduct == null) {
			return;
		}
		Parent ProductRegisterView = null;
		Stage ProductRegisterStage = null;
		try {
			ProductRegisterView = FXMLLoader.load(getClass().getResource("/view/ProductRegister.fxml"));
			ProductRegisterStage = new Stage(StageStyle.UTILITY);
			ProductRegisterStage.initModality(Modality.WINDOW_MODAL);
			ProductRegisterStage.initOwner(btnProductRegisterOpen.getScene().getWindow());
			ProductRegisterStage.setTitle("제품 정보 수정");

			TextField textProductName = (TextField) ProductRegisterView.lookup("#txtProductName");
			TextField textProductPrice = (TextField) ProductRegisterView.lookup("#txtProductPrice");
			TextField textProductCompany = (TextField) ProductRegisterView.lookup("#txtProductCompany");
			TextArea textProductIngredient = (TextArea) ProductRegisterView.lookup("#txtProductIngredient");
			ComboBox<String> comboBoxEfficacy = (ComboBox<String>) ProductRegisterView.lookup("#cbEfficacy");
			ImageView imageProductPhoto = (ImageView) ProductRegisterView.lookup("#imgProductPhoto");
			Button buttonProductNameCheck = (Button) ProductRegisterView.lookup("#btnProductNameCheck");
			Button buttonProductPhotoRegister = (Button) ProductRegisterView.lookup("#btnProductPhotoRegister");
			Button buttonProductPhotoDelete = (Button) ProductRegisterView.lookup("#btnProductPhotoDelete");
			Button buttonProductRegister = (Button) ProductRegisterView.lookup("#btnProductRegister");
			// 효능 콤보박스 보기 세팅
			ObservableList<String> efficacy = FXCollections.observableArrayList("집중력 강화", "피로 회복", "통증 완화");
			comboBoxEfficacy.setItems(efficacy);
			textProductIngredient.setWrapText(true);
			productData = FXCollections.observableArrayList();
			// 선택된 제품의 정보로 세팅해줌.
			textProductName.setText(selectProduct.get(0).getProductName());
			textProductPrice.setText(selectProduct.get(0).getProductPrice());
			textProductCompany.setText(selectProduct.get(0).getProductCompany());
			textProductIngredient.setText(selectProduct.get(0).getProductIngredient());
			comboBoxEfficacy.setValue(selectProduct.get(0).getProductEfficacy());
			imageProductPhoto.setImage(localImage);
			// 제품이름은 변경할 수 없게함. 중복확인 버튼도 누를수 없게함.
			textProductName.setEditable(false);
			buttonProductNameCheck.setDisable(true);
			// 사진 등록 버튼
			buttonProductPhotoRegister.setOnAction(e1 -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(buttonProductPhotoRegister.getScene().getWindow());
					if (selectedFile != null) {
						// 이미지 파일 경로
						localUrl = selectedFile.toURI().toURL().toString();
					}
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				}
				localImage = new Image(localUrl, false);
				imageProductPhoto.setImage(localImage);
				imageProductPhoto.setLayoutX(381);
				imageProductPhoto.setLayoutY(42);
				imageProductPhoto.setFitHeight(220);
				imageProductPhoto.setFitWidth(193);
				imageProductPhoto.setDisable(false);
				photoDelete = false;
			});
			// 사진 삭제 버튼
			buttonProductPhotoDelete.setOnAction(e1 -> {
				fileName = imageSave(null);
				imageProductPhoto.setImage(null);
				photoDelete = true;
			});
			// 제품 등록 버튼
			buttonProductRegister.setOnAction(e1 -> {
				// 이미지 파일 저장
				String fileName = imageSave(selectedFile);
				File dirMake = new File(dirSave.getAbsolutePath());
				// 이미지 저장 폴더 생성
				if (!dirMake.exists()) {
					dirMake.mkdir();
				}
				if (photoDelete == true) {
					DBUtil.alertDisplay(1, "등록실패", "사진이 등록되지 않았습니다", "다시 확인해주세요.");
					return;
				}
				try {
					if (textProductName.getText().equals("") || textProductPrice.getText().equals("")
							|| comboBoxEfficacy.getValue().toString().equals("")
							|| textProductCompany.getText().equals("") || textProductIngredient.getText().equals("")
							|| fileName == null) {
						throw new Exception();
					} else {
						ProductVO pvo = new ProductVO(textProductName.getText(), textProductPrice.getText(),
								comboBoxEfficacy.getValue().toString(), textProductCompany.getText(),
								textProductIngredient.getText(), fileName);
						// DB를 부르는 명령
						productDAO = new ProductDAO();
						// 데이터베이스 테이블에 입력값을 입력하는 함수
						productDAO.getProductDelete(pvo.getProductName());
						productDAO.getProductRegiste(pvo);
						if (pvo != null) {
							productTableView.setItems(productData);
							// 알림창 띄우기
							((Stage) buttonProductRegister.getScene().getWindow()).close();
							DBUtil.alertDisplay(1, "등록성공", "테이블뷰에 등록성공", "등록되었습니다.");
							productTotalList();
						} else {
							DBUtil.alertDisplay(1, "등록실패", "테이블뷰에 등록실패", "다시 시도해주세요.");
						}

					}
				} catch (Exception e2) {
					DBUtil.alertDisplay(1, "등록실패", "제품 정보 모두 기재요망", e2.toString());
				}

			});
			// 제품 이름 중복 체크 버튼
			buttonProductNameCheck.setOnAction(e1 -> {
				ProductDAO productDAO = new ProductDAO();
				int i = productDAO.getProductNameSearch(textProductName.getText());

				if (i == 1) {
					DBUtil.alertDisplay(1, "제품명 중복", "이미 등록된 제품명입니다.", "다시 확인해주세요.");
					return;
				} else if (i == -1) {
					DBUtil.alertDisplay(1, "제품명 등록 가능", "등록할 수 있는 제품명입니다.", "다른 항목도 입력해주세요.");
				}
			});

			Scene scene = new Scene(ProductRegisterView);
			ProductRegisterStage.setScene(scene);
			ProductRegisterStage.setResizable(true);
			ProductRegisterStage.show();

		} catch (IOException e2) {
			DBUtil.alertDisplay(1, "제품 수정창 부르기 실패", "제품 수정창 부르기 실패", e2.toString());
		}

	}

	// 제품관리 테이블뷰를 눌렀을때
	public void handlerProductTableViewPressedAction(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		selectedIndex = productTableView.getSelectionModel().getSelectedIndex();
		selectProduct = productTableView.getSelectionModel().getSelectedItems();
		String fileName = selectProduct.get(0).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName);
		if (selectedFile != null) {
			// 이미지 파일 경로/
			try {
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgProduct.setImage(localImage);
				imgProduct.setFitHeight(250);
				imgProduct.setFitWidth(200);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
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
			fileName = "product" + System.currentTimeMillis() + "_" + file.getName();
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
		productTableView.setItems(productData);
	}

	// 3번째 '메세지관리' 함수등록
	// ===================================================================
	// 메세지탭 테이블 뷰 세팅
	public void messageTableViewSetting() {
		TableColumn messageId = new TableColumn("User ID");
		messageId.setPrefWidth(110);
		messageId.setStyle("-fx-alignment: CENTER;");
		messageId.setCellValueFactory(new PropertyValueFactory("id"));

		TableColumn message = new TableColumn("Message");
		message.setPrefWidth(569);
		message.setStyle("-fx-alignment: CENTER;");
		message.setCellValueFactory(new PropertyValueFactory("message"));

		// 테이블설정 컬럼들 객체를 테이블뷰에 리스트추가 및 항목추가
		messageTableView.setItems(MessageData);
		messageTableView.getColumns().addAll(messageId, message);
	}
	// 메세지 삭제 버튼
	public void handlerBtnMessageDeleteAction(ActionEvent e) {
		try {
			selectMessage = messageTableView.getSelectionModel().getSelectedItems();
			MessageDAO messageDAO = new MessageDAO();
			messageDAO.getMessageDelete(selectMessage.get(0).getNo());
			MessageData.removeAll(MessageData);
			messageTotalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "메세지 삭제", "메세지 삭제 오류", "다시 확인해주세요.");
		}
	}
	// 메세지 테이블뷰에 뿌려주기
	public void messageTotalList() {
		MessageData = FXCollections.observableArrayList();
		ArrayList<MessageVO> list2 = null;
		MessageDAO messageDAO = new MessageDAO();
		MessageVO messageVO = null;
		list2 = messageDAO.getMessageCheck();
		if (list2 == null) {
			DBUtil.alertDisplay(1, "경고", "데이터베이스 가져오기 오류", "점검요망");
			return;
		}
		for (int i = 0; i < list2.size(); i++) {
			MessageData.remove(MessageData);
			messageVO = list2.get(i);
			MessageData.add(messageVO);
		}
		messageTableView.setItems(MessageData);
	}


}