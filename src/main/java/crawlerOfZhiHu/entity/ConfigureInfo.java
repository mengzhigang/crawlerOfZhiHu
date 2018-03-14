package crawlerOfZhiHu.entity;

/**
 * 配置信息实体类
 * @author mzg96
 *
 */
public class ConfigureInfo {

	/**
	 * 登录界面地址
	 */
	private String url;
	
	/**
	 * 文件本地输出目录
	 */
	private String outPutFileDirectryRootPath;
	
	/**
	 * 请求信息日志输出目录
	 */
	private String logOutPutFileDirectryPah;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOutPutFileDirectryRootPath() {
		return outPutFileDirectryRootPath;
	}

	public void setOutPutFileDirectryRootPath(String outPutFileDirectryRootPath) {
		this.outPutFileDirectryRootPath = outPutFileDirectryRootPath;
	}

	public String getLogOutPutFileDirectryPah() {
		return logOutPutFileDirectryPah;
	}

	public void setLogOutPutFileDirectryPah(String logOutPutFileDirectryPah) {
		this.logOutPutFileDirectryPah = logOutPutFileDirectryPah;
	}

	@Override
	public String toString() {
		return "ConfigureInfo [url=" + url + ", outPutFileDirectryRootPath=" + outPutFileDirectryRootPath
				+ ", logOutPutFileDirectryPah=" + logOutPutFileDirectryPah + "]";
	}

	
	
	
}
