package model;

public class RecommendVO {
	private int num;
	private String productName;
	private int recommendCount;
	private String productEfficacy;
	private String productImage;
	
	public RecommendVO(String productName) {
		super();
		this.productName = productName;
	}
	public RecommendVO(String productName, int recommendCount, String productEfficacy, String productImage) {
		super(); 
		this.productName = productName;
		this.recommendCount = recommendCount;
		this.productEfficacy = productEfficacy;
		this.productImage = productImage;
	}
	public RecommendVO(int num, String productName, int recommendCount, String productEfficacy, String productImage) {
		super();
		this.num = num;
		this.productName = productName;
		this.recommendCount = recommendCount;
		this.productEfficacy = productEfficacy;
		this.productImage = productImage;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getRecommendCount() {
		return recommendCount;
	}
	public void setRecommendCount(int recommendCount) {
		this.recommendCount = recommendCount;
	}
	public String getProductEfficacy() {
		return productEfficacy;
	}
	public void setProductEfficacy(String productEfficacy) {
		this.productEfficacy = productEfficacy;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
}
