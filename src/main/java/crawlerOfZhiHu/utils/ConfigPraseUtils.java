package crawlerOfZhiHu.utils;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import crawlerOfZhiHu.entity.ConfigureInfo;
import crawlerOfZhiHu.entity.User;
import crawlerOfZhiHu.resource.PropertyPath;

public class ConfigPraseUtils {

		private ConfigPraseUtils(){}
		 
		private static ConfigureInfo configureInfo;
		 
		private static User user;
		
		public static void parseConfigure() {
			Properties properties = new Properties();
			try {
				FileInputStream fis = new FileInputStream(ConfigPraseUtils.class.getResource("/").getPath() + PropertyPath.PROPERTY_PATHY);
				properties.load(fis);
				configureInfo = new ConfigureInfo();
				Field [] fields = ConfigureInfo.class.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					field.set(configureInfo, properties.getProperty(field.getName()));
				}
				
				user = new User();
				fields = User.class.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					field.set(user, properties.get(field.getName()));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static ConfigureInfo getConfigureInfo() {
			return configureInfo;
		}
		
		public static User getUser() {
			return user;
		}
}
