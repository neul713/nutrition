package model;

import javafx.scene.image.ImageView;

public class ProductVO {
	private String productName;
	private String productPrice;
	private String productEfficacy;
	private String productCompany;
	private String productIngredient;
	private String productImage;

	public ProductVO(String productName, String productPrice, String productEfficacy, String productCompany,
			String productIngredient, String productImage) {
		super();
		this.productName = productName;
		this.productPrice = productPrice;
		this.productEfficacy = productEfficacy;
		this.productCompany = productCompany;
		this.productIngredient = productIngredient;
		this.productImage = productImage;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductEfficacy() {
		return productEfficacy;
	}

	public void setProductEfficacy(String productEfficacy) {
		this.productEfficacy = productEfficacy;
	}

	public String getProductCompany() {
		return productCompany;
	}

	public void setProductCompany(String productCompany) {
		this.productCompany = productCompany;
	}

	public String getProductIngredient() {
		return productIngredient;
	}

	public void setProductIngredient(String productIngredient) {
		this.productIngredient = productIngredient;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	

}