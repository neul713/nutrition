package model;

public class UserVO {
	private String id;
	private String password;
	private String passwordCheck;
	private String name;
	private String birth;
	private String phoneNumber;
	private String survey;
	private String userImage;
	


	public UserVO(String id, String password, String passwordCheck, String name, String birth, String phoneNumber,
			String survey, String userImage) {
		super();
		this.id = id;
		this.password = password;
		this.passwordCheck = passwordCheck;
		this.name = name;
		this.birth = birth;
		this.phoneNumber = phoneNumber;
		this.survey = survey;
		this.userImage = userImage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public void setPasswordCheck(String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSurvey() {
		return survey;
	}

	public void setSurvey(String survey) {
		this.survey = survey;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
}
