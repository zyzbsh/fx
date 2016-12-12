package fxtrader.com.app.entity;

public class DataVo {

	private int code;
	private String message;
	private String object;
	private boolean success;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "DataVo{" +
				"code=" + code +
				", message='" + message + '\'' +
				", object='" + object + '\'' +
				", success=" + success +
				'}';
	}
}
