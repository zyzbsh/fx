package fxtrader.com.app.entity;

/**
 * 个人信息
 * 
 * @author wq
 * 
 */

public class UserInfoVo {

	public final static String USERINFOVO = "UserInfoVo";
	public final static String ID = "id";
	public final static String USERNAME = "userName";
	public final static String NICKNAME = "nickName";
	public final static String SEX = "sex";
	public final static String AGE = "age";
	public final static String AVATARPATH = "avatarPath";
	public final static String GOLDBALANCE = "goldBalance";
	public final static String PASSWORD = "password";
	public final static String STAR = "star";

	private int id;
	private String userName;
	private String nickName;
	private int sex;
	private String age;
	private String avatarPath;
	private String goldBalance;
	private int star;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getGoldBalance() {
		return goldBalance;
	}

	public void setGoldBalance(String goldBalance) {
		this.goldBalance = goldBalance;
	}

	public int getStar() {
		return this.star;
	}

	public void setStar(int star) {
		this.star = star;
	}

}
