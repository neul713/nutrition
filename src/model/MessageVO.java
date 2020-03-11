package model;

public class MessageVO {
	private int no;
	private String id;
	private String message;
	public MessageVO(String id, String message) {
		super();
		this.id = id;
		this.message = message;
	}
	public MessageVO(int no, String id, String message) {
		super();
		this.no = no;
		this.id = id;
		this.message = message;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
