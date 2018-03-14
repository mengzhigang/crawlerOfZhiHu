package crawlerOfZhiHu.entity;

import java.util.List;

/**
 * 登录用户实体类
 * @author mzg96
 *
 */
public class User {
	
	/**
	 * 登录用户名
	 */
	private String userName;
	
	/**
	 * 登录密码
	 */
	private String password;
	
	/**
	 * 登录成功是产生的cookie
	 */
	private List<String> cookie;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getCookie() {
		return cookie;
	}

	public void setCookie(List<String> cookie) {
		this.cookie = cookie;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", cookie=" + cookie + "]";
	}

	
	
	
}
