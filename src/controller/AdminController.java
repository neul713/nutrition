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
	// ȸ������ ��
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
	private String localUrl = ""; // �̹��� ���� ���
	private Image localImage;
	// ��ǰ���� ��
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

	// ��ǰ �̹��� ������ ������ �Ű������� ���� ��ü ����
	private File dirSave = new File("c:/productImages");
	// �̹��� �ҷ��� ������ ������ ���� ��ü ����
	private File file = null;
	private String fileName;
	// �޼��� ��
	@FXML
	private TableView<MessageVO> messageTableView = new TableView<>();
	@FXML
	private Button btnMessageDelete;
	private ObservableList<MessageVO> MessageData;
	private ObservableList<MessageVO> selectMessage;
	// ���� ���� ��ư�� ������ ���� ������ ������� ������ ��ǰ��� �����ʰ� �ϴ� ����
	private boolean photoDelete = false;
	// ��ǰ �̸� �ߺ� üũ ����
	private boolean productCheck = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 1��° �� 'ȸ������' �̺�Ʈ ���
		// ====================================
		// ȸ������ ���̺� �� ����
		tableViewSetting();
		// ȸ�� ������ ���̺� �信 �ѷ��ֱ�
		totalList();
		// ���� �⺻ �̹��� ����
		userImageViewInit();
		// �˻� ��ư�� ��������
		btnUserSearch.setOnAction(e -> {
			handlerBtnSearchAction(e);
		});
		// ���� ��ư�� ��������
		btnUserDelete.setOnAction(e -> {
			handlerBtnDeleteAction(e);
		});
		// ��ü���� ��ư�� ��������
		btnUserAllView.setOnAction(e -> {
			handlerBtnAllViewAction(e);
		});
		// ��Ʈ���� ��ư�� ��������
		btnChartView.setOnAction(e -> {
			handlerBtnChartViewAction(e);
		});
		// ���̺� �並 ��������
		userTableView.setOnMousePressed(e -> {
			handlerUserTableViewPressedAction(e);
		});
		// 2��° �� '��ǰ����' �̺�Ʈ ���
		// ====================================
		// ��ǰ���� ���̺�� ����
		productTableViewSetting();
		// ��ǰ ������ ���̺� �信 �ѷ��ֱ�
		productTotalList();
		// ��ǰ �⺻ �̹��� ����
		productImageViewInit();
		// �˻� ��ư�� ��������
		btnProductSearch.setOnAction(e -> {
			handlerBtnProductSearchAction(e);
		});
		// ���� ��ư�� ��������
		btnProductDelete.setOnAction(e -> {
			handlerBtnProductDeleteAction(e);
		});
		// ��ü���� ��ư�� ��������
		btnProductAllView.setOnAction(e -> {
			handlerBtnProductAllViewAction(e);
		});
		// ��ǰ��� ��ư�� ��������
		btnProductRegisterOpen.setOnAction(e -> {
			handlerBtnProductRegisterOpenAction(e);
		});
		// ���� ��ư�� ��������
		btnProductEdit.setOnAction(e -> {
			handlerBtnProductEditAction(e);
		});
		// ���̺�並 ��������
		productTableView.setOnMousePressed(e -> {
			handlerProductTableViewPressedAction(e);
		});
		// 3��° �� '�޼�������' �̺�Ʈ
		messageTableViewSetting();
		// �޼��� ���� ��ư
		btnMessageDelete.setOnAction(e -> {
			handlerBtnMessageDeleteAction(e);
		});
		//�޼��� ������ ���̺�信 �ѷ��ֱ�
		messageTotalList();
	}

	// 1��° �� 'ȸ������' �Լ� ���
	// ======================================================
	// ȸ�� �⺻�̹��� ����
	public void userImageViewInit() {
		localUrl = "/images/defaultPhoto.png";
		localImage = new Image(localUrl, false);
		imgUser.setImage(localImage);
		imgUser.setFitHeight(250);
		imgUser.setFitWidth(200);
	}

	// ȸ������ ���̺� �� ����
	public void tableViewSetting() {

		TableColumn colId = new TableColumn("���̵�");
		colId.setPrefWidth(100);
		colId.setStyle("-fx-alignment: CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("id"));

		TableColumn colName = new TableColumn("�̸�");
		colName.setPrefWidth(100);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colBirth = new TableColumn("�������");
		colBirth.setPrefWidth(158);
		colBirth.setStyle("-fx-alignment: CENTER;");
		colBirth.setCellValueFactory(new PropertyValueFactory("birth"));

		TableColumn colPhoneNumber = new TableColumn("��ȭ��ȣ");
		colPhoneNumber.setPrefWidth(200);
		colPhoneNumber.setStyle("-fx-alignment: CENTER;");
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));

		TableColumn colSurvey = new TableColumn("���԰��");
		colSurvey.setPrefWidth(120);
		colSurvey.setStyle("-fx-alignment: CENTER;");
		colSurvey.setCellValueFactory(new PropertyValueFactory("survey"));

		userTableView.setItems(data);
		userTableView.getColumns().addAll(colId, colName, colBirth, colPhoneNumber, colSurvey);
	}

	// ȸ������ �˻� ��ư�� ��������
	public void handlerBtnSearchAction(ActionEvent e) {
		try {
			ArrayList<UserVO> list = new ArrayList<UserVO>();
			UserDAO userDAO = new UserDAO();
			list = userDAO.getUserCheck(txtUserSearch.getText());
			if (list == null) {
				throw new Exception("ã���ô� ȸ���� �����ϴ�.");
			}
			data.removeAll(data);
			for (UserVO svo : list) {
				data.add(svo);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// ȸ������ ���� ��ư�� ��������
	public void handlerBtnDeleteAction(ActionEvent e) {
		try {
			selectUser = userTableView.getSelectionModel().getSelectedItems();
			UserDAO userDAO = new UserDAO();
			userDAO.getUserDelete(selectUser.get(0).getId());
			data.removeAll(data);
			totalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "ȸ�� ����", "ȸ�� ���� ����", "�ٽ� Ȯ�����ּ���.");
		}
	}

	// ȸ������ ��ü���� ��ư�� ��������
	public void handlerBtnAllViewAction(ActionEvent e) {
		try {
			data.removeAll(data);
			totalList();
			userImageViewInit();
		} catch (Exception e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "ȸ�� ��ü����", "ȸ�� ��ü���� ����", "�ٽ� Ȯ�����ּ���.");
		}
	}

	// ȸ������ ��Ʈ���� ��ư�� ��������
	public void handlerBtnChartViewAction(ActionEvent e) {
		try {
			Parent pieChartRoot = FXMLLoader.load(getClass().getResource("/view/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnChartView.getScene().getWindow());
			stage.setTitle("������� ���԰�� ");

			PieChart pieChart = (PieChart) pieChartRoot.lookup("#pieChart");

			ArrayList<PieChartVO> list = null;
			UserDAO userDAO = new UserDAO();
			list = userDAO.getSurveyResult();

			if (list == null) {
				DBUtil.alertDisplay(1, "���", "�����ͺ��̽� ������Ʈ �������� ����", "�ٽ� Ȯ�����ּ���.");
				return;
			}

			ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				pieData.add(new PieChart.Data(list.get(i).getSurvey(), list.get(i).getCount()));
			}
			pieChart.setData(pieData);
			// �׷��� �׸���
			Scene scene = new Scene(pieChartRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	// ȸ�� ���� ���̺� �並 ��������
	public void handlerUserTableViewPressedAction(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		selectUser = userTableView.getSelectionModel().getSelectedItems();
		String fileName = selectUser.get(0).getUserImage();
		selectedFile = new File("C:/userImages/" + fileName);
		if (selectedFile != null) {
			// �̹��� ���� ���/
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

	// ȸ�� ���� ���̺�信 �ѷ��ֱ�
	public void totalList() {
		data = FXCollections.observableArrayList();
		ArrayList<UserVO> list = null;
		UserDAO userDAO = new UserDAO();
		UserVO userVO = null;
		list = userDAO.getUserTotal();

		if (list == null) {
			DBUtil.alertDisplay(1, "���", "�����ͺ��̽� �������� ����", "�ٽ� Ȯ�����ּ���.");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			userVO = list.get(i);
			data.add(userVO);
		}
		userTableView.setItems(data);
	}

	// 2��° '��ǰ����' �Լ����
	// ===================================================================
	// ��ǰ �⺻�̹��� ����
	public void productImageViewInit() {
		localUrl = "/images/vitaminImg.jpg";
		localImage = new Image(localUrl, false);
		imgProduct.setImage(localImage);
		imgProduct.setFitHeight(250);
		imgProduct.setFitWidth(200);
	}

	// ��ǰ���� ���̺� �� ����
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
		productTableView.setItems(productData);
		productTableView.getColumns().addAll(colProductName, colProductPrice, colProductEfficacy, colProductCompany,
				colProductIngredient);
	}

	// ��ǰ���� ��ü���� ��ư�� ��������
	public void handlerBtnProductAllViewAction(ActionEvent e) {
		try {
			productData.removeAll(productData);
			productTotalList();
			productImageViewInit();
		} catch (Exception e2) {
			e2.printStackTrace();
			DBUtil.alertDisplay(1, "��ǰ ��ü����", "��ǰ ��ü���� ����", "�ٽ� Ȯ�����ּ���.");
		}
	}

	// ��ǰ���� ���� ��ư�� ��������
	public void handlerBtnProductDeleteAction(ActionEvent e) {
		try {
			selectProduct = productTableView.getSelectionModel().getSelectedItems();
			ProductDAO productDAO = new ProductDAO();
			productDAO.getProductDelete(selectProduct.get(0).getProductName());
			productData.removeAll(productData);
			productTotalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "��ǰ ����", "��ǰ ���� ����", "�ٽ� Ȯ�����ּ���.");
		}

	}

	// ��ǰ���� �˻� ��ư�� ��������
	public void handlerBtnProductSearchAction(ActionEvent e) {
		try {
			ArrayList<ProductVO> list2 = new ArrayList<ProductVO>();
			ProductDAO productDAO = new ProductDAO();
			list2 = productDAO.getProductCheck(txtProductSearch.getText());
			if (list2 == null) {
				throw new Exception("ã���ô� ��ǰ�� �����ϴ�.");
			}
			productData.removeAll(productData);
			for (ProductVO pvo : list2) {
				productData.add(pvo);
			}
		} catch (Exception e2) {
			e.toString();
		}
	}

	// ��ǰ��� ��ư�� ��������
	public void handlerBtnProductRegisterOpenAction(ActionEvent e) {
		Parent ProductRegisterView = null;
		Stage ProductRegisterStage = null;
		try {
			ProductRegisterView = FXMLLoader.load(getClass().getResource("/view/ProductRegister.fxml"));
			ProductRegisterStage = new Stage(StageStyle.UTILITY);
			ProductRegisterStage.initModality(Modality.WINDOW_MODAL);
			ProductRegisterStage.initOwner(btnProductRegisterOpen.getScene().getWindow());
			ProductRegisterStage.setTitle("��ǰ ���");

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
			// ȿ�� �޺��ڽ� ���� ����
			ObservableList<String> efficacy = FXCollections.observableArrayList("�� ��� ��ȭ", "�Ƿ� ȸ��", "�׻�ȭ ��� ��ȭ",
					"�鿪�� ����", "���� ����", "�� �ǰ�");
			comboBoxEfficacy.setItems(efficacy);

			textProductIngredient.setWrapText(true);
			DecimalFormat format = new DecimalFormat("###########");
			// ���� �Է½� ���� ���� �̺�Ʈ ó��
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

			// ���� ��� ��ư
			buttonProductPhotoRegister.setOnAction(e1 -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(buttonProductPhotoRegister.getScene().getWindow());
					if (selectedFile != null) {
						// �̹��� ���� ���
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
			// ���� ���� ��ư
			buttonProductPhotoDelete.setOnAction(e1 -> {
				fileName = imageSave(null);
				imageProductPhoto.setImage(null);
				photoDelete = true;
			});
			// ��ǰ �̸� �ߺ� üũ ��ư
			buttonProductNameCheck.setOnAction(e1 -> {
				ProductDAO productDAO = new ProductDAO();
				int i = productDAO.getProductNameSearch(textProductName.getText());

				if (i == 1) {
					DBUtil.alertDisplay(1, "��ǰ�� �ߺ�", "�̹� ��ϵ� ��ǰ���Դϴ�.", "�ٽ� Ȯ�����ּ���.");
					buttonProductRegister.setDisable(true);
					return;

				} else if (i == -1) {
					DBUtil.alertDisplay(1, "��ǰ�� ��� ����", "����� �� �ִ� ��ǰ���Դϴ�.", "�ٸ� �׸� �Է����ּ���.");
					buttonProductRegister.setDisable(false);
					productCheck = true;
					return;

				}
			});
			// ��ǰ ��� ��ư
			buttonProductRegister.setOnAction(e1 -> {
				// �̹��� ���� ����
				String fileName = imageSave(selectedFile);
				File dirMake = new File(dirSave.getAbsolutePath());
				// �̹��� ���� ���� ����
				if (!dirMake.exists()) {
					dirMake.mkdir();
				}
				if (photoDelete == true) {
					DBUtil.alertDisplay(1, "��Ͻ���", "������ ��ϵ��� �ʾҽ��ϴ�", "�ٽ� Ȯ�����ּ���.");
					return;
				}
				if (productCheck == false) {
					DBUtil.alertDisplay(1, "��� ����", "��ǰ�� �ߺ�üũ�� ���� �ʾҽ��ϴ�.", "��ǰ�� �ߺ�üũ�� Ȯ�����ּ���.");
					return;
				}
				try {
					if (textProductName.getText().equals("") || textProductPrice.getText().equals("")
							|| comboBoxEfficacy.getValue().toString().equals("")
							|| textProductCompany.getText().equals("") || textProductIngredient.getText().equals("")
							|| fileName == null) {
						throw new Exception("�� �ȵ�");
					} else {
						ProductVO pvo = new ProductVO(textProductName.getText(), textProductPrice.getText(),
								comboBoxEfficacy.getValue().toString(), textProductCompany.getText(),
								textProductIngredient.getText(), fileName);
						// DB�� �θ��� ���
						productDAO = new ProductDAO();
						// �����ͺ��̽� ���̺� �Է°��� �Է��ϴ� �Լ�
						productDAO.getProductRegiste(pvo);

						if (pvo != null) {

							productTableView.setItems(productData);
							// �˸�â ����
							((Stage) buttonProductRegister.getScene().getWindow()).close();
							DBUtil.alertDisplay(1, "��ϼ���", "���̺�信 ��ϼ���", "��ϵǾ����ϴ�.");
							productTotalList();
						} else {
							DBUtil.alertDisplay(1, "��Ͻ���", "���̺�信 ��Ͻ���", "�ٽ� �õ����ּ���.");
						}
					}

				} catch (Exception e2) {
					DBUtil.alertDisplay(1, "��Ͻ���", "��ǰ ���� ��� ������", e2.toString());
				}

			});

			Scene scene = new Scene(ProductRegisterView);
			ProductRegisterStage.setScene(scene);
			ProductRegisterStage.setResizable(true);
			ProductRegisterStage.show();

		} catch (IOException e2) {
			DBUtil.alertDisplay(1, "��ǰ ���â �θ��� ����", "��ǰ ���â �θ��� ����", e2.toString());
		}
	}

	// ���� ��ư�� ��������
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
			ProductRegisterStage.setTitle("��ǰ ���� ����");

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
			// ȿ�� �޺��ڽ� ���� ����
			ObservableList<String> efficacy = FXCollections.observableArrayList("���߷� ��ȭ", "�Ƿ� ȸ��", "���� ��ȭ");
			comboBoxEfficacy.setItems(efficacy);
			textProductIngredient.setWrapText(true);
			productData = FXCollections.observableArrayList();
			// ���õ� ��ǰ�� ������ ��������.
			textProductName.setText(selectProduct.get(0).getProductName());
			textProductPrice.setText(selectProduct.get(0).getProductPrice());
			textProductCompany.setText(selectProduct.get(0).getProductCompany());
			textProductIngredient.setText(selectProduct.get(0).getProductIngredient());
			comboBoxEfficacy.setValue(selectProduct.get(0).getProductEfficacy());
			imageProductPhoto.setImage(localImage);
			// ��ǰ�̸��� ������ �� ������. �ߺ�Ȯ�� ��ư�� ������ ������.
			textProductName.setEditable(false);
			buttonProductNameCheck.setDisable(true);
			// ���� ��� ��ư
			buttonProductPhotoRegister.setOnAction(e1 -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(buttonProductPhotoRegister.getScene().getWindow());
					if (selectedFile != null) {
						// �̹��� ���� ���
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
			// ���� ���� ��ư
			buttonProductPhotoDelete.setOnAction(e1 -> {
				fileName = imageSave(null);
				imageProductPhoto.setImage(null);
				photoDelete = true;
			});
			// ��ǰ ��� ��ư
			buttonProductRegister.setOnAction(e1 -> {
				// �̹��� ���� ����
				String fileName = imageSave(selectedFile);
				File dirMake = new File(dirSave.getAbsolutePath());
				// �̹��� ���� ���� ����
				if (!dirMake.exists()) {
					dirMake.mkdir();
				}
				if (photoDelete == true) {
					DBUtil.alertDisplay(1, "��Ͻ���", "������ ��ϵ��� �ʾҽ��ϴ�", "�ٽ� Ȯ�����ּ���.");
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
						// DB�� �θ��� ���
						productDAO = new ProductDAO();
						// �����ͺ��̽� ���̺� �Է°��� �Է��ϴ� �Լ�
						productDAO.getProductDelete(pvo.getProductName());
						productDAO.getProductRegiste(pvo);
						if (pvo != null) {
							productTableView.setItems(productData);
							// �˸�â ����
							((Stage) buttonProductRegister.getScene().getWindow()).close();
							DBUtil.alertDisplay(1, "��ϼ���", "���̺�信 ��ϼ���", "��ϵǾ����ϴ�.");
							productTotalList();
						} else {
							DBUtil.alertDisplay(1, "��Ͻ���", "���̺�信 ��Ͻ���", "�ٽ� �õ����ּ���.");
						}

					}
				} catch (Exception e2) {
					DBUtil.alertDisplay(1, "��Ͻ���", "��ǰ ���� ��� ������", e2.toString());
				}

			});
			// ��ǰ �̸� �ߺ� üũ ��ư
			buttonProductNameCheck.setOnAction(e1 -> {
				ProductDAO productDAO = new ProductDAO();
				int i = productDAO.getProductNameSearch(textProductName.getText());

				if (i == 1) {
					DBUtil.alertDisplay(1, "��ǰ�� �ߺ�", "�̹� ��ϵ� ��ǰ���Դϴ�.", "�ٽ� Ȯ�����ּ���.");
					return;
				} else if (i == -1) {
					DBUtil.alertDisplay(1, "��ǰ�� ��� ����", "����� �� �ִ� ��ǰ���Դϴ�.", "�ٸ� �׸� �Է����ּ���.");
				}
			});

			Scene scene = new Scene(ProductRegisterView);
			ProductRegisterStage.setScene(scene);
			ProductRegisterStage.setResizable(true);
			ProductRegisterStage.show();

		} catch (IOException e2) {
			DBUtil.alertDisplay(1, "��ǰ ����â �θ��� ����", "��ǰ ����â �θ��� ����", e2.toString());
		}

	}

	// ��ǰ���� ���̺�並 ��������
	public void handlerProductTableViewPressedAction(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		selectedIndex = productTableView.getSelectionModel().getSelectedIndex();
		selectProduct = productTableView.getSelectionModel().getSelectedItems();
		String fileName = selectProduct.get(0).getProductImage();
		selectedFile = new File("C:/productimages/" + fileName);
		if (selectedFile != null) {
			// �̹��� ���� ���/
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

	// �̹��� ����
	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// �̹��� ���ϸ� ����
			fileName = "product" + System.currentTimeMillis() + "_" + file.getName();
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
		productTableView.setItems(productData);
	}

	// 3��° '�޼�������' �Լ����
	// ===================================================================
	// �޼����� ���̺� �� ����
	public void messageTableViewSetting() {
		TableColumn messageId = new TableColumn("User ID");
		messageId.setPrefWidth(110);
		messageId.setStyle("-fx-alignment: CENTER;");
		messageId.setCellValueFactory(new PropertyValueFactory("id"));

		TableColumn message = new TableColumn("Message");
		message.setPrefWidth(569);
		message.setStyle("-fx-alignment: CENTER;");
		message.setCellValueFactory(new PropertyValueFactory("message"));

		// ���̺��� �÷��� ��ü�� ���̺�信 ����Ʈ�߰� �� �׸��߰�
		messageTableView.setItems(MessageData);
		messageTableView.getColumns().addAll(messageId, message);
	}
	// �޼��� ���� ��ư
	public void handlerBtnMessageDeleteAction(ActionEvent e) {
		try {
			selectMessage = messageTableView.getSelectionModel().getSelectedItems();
			MessageDAO messageDAO = new MessageDAO();
			messageDAO.getMessageDelete(selectMessage.get(0).getNo());
			MessageData.removeAll(MessageData);
			messageTotalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "�޼��� ����", "�޼��� ���� ����", "�ٽ� Ȯ�����ּ���.");
		}
	}
	// �޼��� ���̺�信 �ѷ��ֱ�
	public void messageTotalList() {
		MessageData = FXCollections.observableArrayList();
		ArrayList<MessageVO> list2 = null;
		MessageDAO messageDAO = new MessageDAO();
		MessageVO messageVO = null;
		list2 = messageDAO.getMessageCheck();
		if (list2 == null) {
			DBUtil.alertDisplay(1, "���", "�����ͺ��̽� �������� ����", "���˿��");
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