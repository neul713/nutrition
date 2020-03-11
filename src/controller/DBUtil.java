package controller;

import java.sql.Connection;
import java.sql.DriverManager;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// ����̹��� ����, ���̵�� �н������ ����Ÿ���̽� mysql�� �����û�Ѵ�-> ��ü�������� �ش�.
public class DBUtil {
	// 1. ����̹��� ����  
	private static String driver = "com.mysql.jdbc.Driver";
	// 2. �����ͺ��̽� url ����
	private static String url = "jdbc:mysql://192.168.0.224/nutrition";
	// 3. ����̹��� �����ϰ�, ����Ÿ���̽��� �����ϴ� �Լ�
	public static Connection getConnection() throws Exception {
		// 3.1. ����̹��� ���� 
		Class.forName(driver);
		// 3.2. �����ͺ��̽��� ����
		Connection con = DriverManager.getConnection(url, "root", "123456789");
		return con;
	}

	// �˸�â ����
	public static void alertDisplay(int type, String title, String headerText, String contentText) {
		Alert alert = null;
		switch (type) {
		case 1:
			alert = new Alert(AlertType.WARNING);
			break;
		case 2:
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case 3:
			alert = new Alert(AlertType.ERROR);
			break;
		case 4:
			alert = new Alert(AlertType.NONE);
			break;
		case 5:
			alert = new Alert(AlertType.INFORMATION);
			break;
		default:
			break;
		}
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(headerText + "\n" + contentText);
		alert.setResizable(false);
		alert.showAndWait();
	}

}