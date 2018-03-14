package crawlerOfZhiHu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawlerOfZhiHu.entity.CrawlerQueue;
import crawlerOfZhiHu.utils.ConfigPraseUtils;
import crawlerOfZhiHu.utils.OutPutPathParseUtils;

public class CrawlerStartMain {
	
	public static int index = 0;

	public static void main(String[] args) throws Exception {
		System.out.println("-----------------解析开始-------------------------");
		System.out.println("1.解析配置文件");
		ConfigPraseUtils.parseConfigure();
		
		System.out.println("2.创建模拟客户端");
		CloseableHttpClient client =  HttpClientBuilder.create().build();
	
		System.out.println("3.创建爬虫队列");
		CrawlerQueue queue = new CrawlerQueue();
	
		System.out.println("4.向爬虫对列中传入根节点");
		queue.enQueue(ConfigPraseUtils.getConfigureInfo().getUrl());
		
		//解析
		parse(client,queue);
		
		System.out.println("-----------------解析结束-------------------------");
		client.close();
	}

	private static void parse(CloseableHttpClient client,CrawlerQueue queue)
			throws IOException, ClientProtocolException, Exception, UnsupportedEncodingException {
		
		while(index < queue.length()) {
			
			System.out.println("解析:" + queue.getPathByIndex(index));
			
			if (queue.getPathByIndex(index).endsWith("js")) {
				index++;
				continue;
			} 
			
			HttpGet get = new HttpGet(queue.getPathByIndex(index++));
			get.setHeader("Cookie", "q_c1=05afe16012b04dc8be498027ae55f9f5|1520989233000|1520989233000; _zap=2091bfe8-d8ae-4d49-812b-9c33fa866d80; aliyungf_tc=AQAAAPsdlzUp8AAAw+rkdGkQAoF0HqAp; _xsrf=f2053938-8c4c-45ba-ac35-2dbc453b4333; d_c0=\"APDsmGLpSA2PTrtCJIYlPR3M_hA6tyyDGfI=|1521011318\"; capsion_ticket=\"2|1:0|10:1521011332|14:capsion_ticket|44:YTlhMDViMzcxOWRjNDNiOTg0Y2YxOGNjZjM3N2M3MzY=|153798a5740a3abff15a4204558601d2ab7b26ef13de9cec086dabbe05882c53\"; z_c0=\"2|1:0|10:1521011864|4:z_c0|92:Mi4xdWxfZ0F3QUFBQUFBOE95WVl1bElEU1lBQUFCZ0FsVk5tQnFXV3dDa0l1ZEtCV0FMWk1hcGhwYzA4bmctZVJPTWNB|0cfae660c8290eb3be7a58a8870ff39e872c77a0bdd39df2924c07763fa4dbda\"");
			HttpResponse response =  client.execute(get);
			
			//将头文件单独写出来
			HeaderToFile(get, response);
			
			parseHTMLAndPutUrlIntoCrawlerQueue(get, response, queue);
		}
		
	}

	private static void parseHTMLAndPutUrlIntoCrawlerQueue(HttpGet get, HttpResponse response , CrawlerQueue queue)
			throws Exception, UnsupportedEncodingException, IOException {
		File file = OutPutPathParseUtils.parseUrlToFile(get.getURI().toString());		
		
		//判断资源类型
		String contextType = response.getHeaders("Content-Type")[0].getValue();
		if (contextType == null) {
			contextType = response.getHeaders("content-Type")[0].getValue();
		}
		
		if ("text/html; charset=utf-8".equals(contextType) || "text/html".equals(contextType) ) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			String line = null;
			while( (line = reader.readLine()) != null) {
				writer.write(line);
			}		
			writer.close();
			Document document = Jsoup.parse(file, "UTF8");
			
			Elements jsElements = document.select("[src]");
			
			for (Element element : jsElements) {
				queue.enQueue(element.attr("src"));
			}
			
			Elements sourceElements = document.select("[href]");
			for (Element element : sourceElements) {
				queue.enQueue(element.attr("href"));
			}
		} else if("image/jpeg".equals(contextType)) {
			InputStream in = response.getEntity().getContent();
			OutputStream out =  (new FileOutputStream("D:/zhihu/"+ System.currentTimeMillis() +".jpg"));
			byte [] b = new byte[1024];
			int len = in.read(b);
			for (; len > 0; len = in.read(b)) {
				out.write(b, 0, len);
				out.flush();
			}
			
			out.close();
			
		}
		
		
		
	}
		
	
	/**
	 * 解析头部文件，并将头部文件写到目标硬盘上
	 * @param get
	 * @param response
	 * @throws IOException
	 */
	private static void HeaderToFile(HttpGet get, HttpResponse response) throws IOException {
		File logOutPutFileDirectryPah = new File(ConfigPraseUtils.getConfigureInfo().getLogOutPutFileDirectryPah());
		
		if (!logOutPutFileDirectryPah.exists()) {
			logOutPutFileDirectryPah.mkdirs();
		}
		
		File requestAnResponseInfoLog  = new File(ConfigPraseUtils.getConfigureInfo().getLogOutPutFileDirectryPah() + URLEncoder.encode(get.getURI().toString()));
		
		BufferedWriter writer =  new BufferedWriter(new FileWriter(requestAnResponseInfoLog));
		
		Header [] requestHeaders = get.getAllHeaders();
		writer.write(ConfigPraseUtils.getConfigureInfo().getUrl());
		writer.newLine();
		writer.write("requestHeaders:");
		writer.newLine();
		for (Header header : requestHeaders) {
			writer.write("\t" + header.getName() + ":" + header.getValue());
			writer.newLine();
		}
		
		Header [] responseHeaders = response.getAllHeaders();
		writer.newLine();
		writer.write("responseHeaders:");
		writer.newLine();
		writer.write("\t" + response.getStatusLine().getStatusCode());
		writer.newLine();
		for (Header header : responseHeaders) {
			writer.write("\t" + header.getName() + ":" + header.getValue());
			writer.newLine();
		}
		
		writer.close();
	}
}
