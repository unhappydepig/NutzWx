package cn.xuetang.common.util;

import org.nutz.log.Log;
import org.nutz.log.Logs;

public class SendUtil extends Thread{

	private  final static Log log = Logs.get();
	private String type;
	private String url;
	private String key;

	public SendUtil(String t, String k, String u) {
		this.type = t;
		this.key = k;
		this.url = u;
	}

	public void run() {
		try {
			UrlUtil.getOneHtml(url + "?key=" + key + "&type=" + type, "UTF-8");
		} catch (Exception e) {
			log.error(e);
		}
	}

}
