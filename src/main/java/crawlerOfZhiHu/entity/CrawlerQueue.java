package crawlerOfZhiHu.entity;

import java.util.LinkedList;

/**
 * 爬虫队列
 * @author mzg96
 *
 */
public class CrawlerQueue {
	
	private LinkedList<String> queue = new LinkedList<String>();
	
	/**
	 *入队列
	 */
	public void enQueue(String path) {
		if(!queue.contains(path) && path.startsWith("http"))
			queue.add(path);
	}
	
	/**
	 * 对列长度
	 */
	public int length() {
		return queue.size();
	}
	
	
	/**
	 * 获得索引为index的元素
	 */
	public String getPathByIndex(int index) {
		return queue.get(index);
	}
}
