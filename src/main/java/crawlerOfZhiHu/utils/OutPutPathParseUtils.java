package crawlerOfZhiHu.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLEncoder;

public class OutPutPathParseUtils {

	private OutPutPathParseUtils() {}
	
	public static File parseUrlToFile(String url) throws Exception {
		URL url0 = new URL(url);
		String filePath =url0.getFile();
		int index =filePath.lastIndexOf("/");
		String dir = null;
		File file = null;
		if(index > 0) {
			dir = filePath.substring(0, index);
			File parentFile =  new File(ConfigPraseUtils.getConfigureInfo().getOutPutFileDirectryRootPath() + dir);
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			file = new File(ConfigPraseUtils.getConfigureInfo().getOutPutFileDirectryRootPath() + dir + "/" + URLEncoder.encode(filePath.substring(index)));
			if (!file.exists()) {
				file.createNewFile();
			}
		} else {
			file = new File(ConfigPraseUtils.getConfigureInfo().getOutPutFileDirectryRootPath()  + URLEncoder.encode(url0.getHost()));
			if (!file.exists()) {
				file.createNewFile();
			}
		}
		return file;
	}
	
}
