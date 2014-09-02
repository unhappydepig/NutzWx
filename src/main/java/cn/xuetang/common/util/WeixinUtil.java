package cn.xuetang.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.xuetang.modules.app.bean.App_info;

/**
 * 微信接口工具类 Created by Wizzer on 14-4-4.
 */
public class WeixinUtil{
	private final static Log log = Logs.get();

	/**
	 * 向微信用户推送一条文本信息，48小时内互动
	 * 
	 * @param dao
	 *            数据库
	 * @param appid
	 *            微信接口
	 * @param toUser
	 *            接收人
	 * @param content
	 *            文本内容
	 * @return
	 */
	public static String PushTxt(String access_token, int appid, String toUser, String content) {
		try {
			Map<String, Object> obj = new HashMap<String, Object>();
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", content);
			obj.put("touser", toUser);
			obj.put("msgtype", "text");
			obj.put("text", contentMap);
			Request req = Request.create("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + access_token, Request.METHOD.POST);
			req.setData(Json.toJson(obj));
			// System.out.println(Json.toJson(obj));
			// req.getParams().put("access_token", access_token);
			Response resp = Sender.create(req).send();
			if (resp.isOK()) {
				return Strings.sNull(resp.getContent());
			}
		} catch (Exception e) {
			log.error(e);
			return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
		}
		return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
	}

	/**
	 * 上传图文消息素材，高级群发接口
	 * 
	 * @param dao
	 *            数据库
	 * @param appid
	 *            微信接口
	 * @param list
	 *            文章列表
	 * @return
	 */
	public static String PushNews(String access_token, List<Object> list) {
		try {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("articles", list);
			Request req = Request.create("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + access_token, Request.METHOD.POST);
			req.setData(Json.toJson(obj));
			System.out.println(Json.toJson(obj));
			// req.getParams().put("access_token", access_token);
			Response resp = Sender.create(req).send();
			if (resp.isOK()) {
				return Strings.sNull(resp.getContent());
			}
		} catch (Exception e) {
			log.error(e);
			return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
		}
		return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
	}

	/**
	 * 向用户推送文章内容，高级群发接口
	 * 
	 * @param dao
	 *            数据库
	 * @param appid
	 *            微信接口
	 * @param obj
	 *            推送内容
	 * @return
	 */
	public static String PushUser(String access_token, Map<String, Object> obj) {
		try {
			Request req = Request.create("https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + access_token, Request.METHOD.POST);
			req.setData(Json.toJson(obj));
			log.debug(Json.toJson(obj));
			// req.getParams().put("access_token", access_token);
			Response resp = Sender.create(req).send();
			if (resp.isOK()) {
				return Strings.sNull(resp.getContent());
			}
		} catch (Exception e) {
			log.error(e);
			return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
		}
		return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
	}

	/**
	 * 将视频media_id、title、description上传获取新media_id
	 * 
	 * @param dao
	 *            数据库
	 * @param appid
	 *            微信接口
	 * @param media_id
	 * @param title
	 * @param description
	 * @return
	 */
	public static String PushVideo(String access_token, String media_id, String title, String description) {
		try {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("media_id", media_id);
			obj.put("title", title);
			obj.put("description", description);
			Request req = Request.create("https://api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=" + access_token, Request.METHOD.POST);
			req.setData(Json.toJson(obj));
			System.out.println(Json.toJson(obj));
			// req.getParams().put("access_token", access_token);
			Response resp = Sender.create(req).send();
			if (resp.isOK()) {
				return Strings.sNull(resp.getContent());
			}
		} catch (Exception e) {
			log.error(e);
			return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
		}
		return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
	}


	/**
	 * 获取access_token
	 * 
	 * @param info
	 * @return
	 */
	public static String getAccess_token(App_info info) {
		try {
			String res = UrlUtil.getOneHtml("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + info.getApp_key() + "&secret=" + info.getApp_secret(), "UTF-8");
			Map<String, Object> map = Json.fromJson(Map.class, res);
			return Strings.sNull(map.get("access_token"));
		} catch (Exception e) {
			log.error(e);
		}
		return "";
	}

	/**
	 * 获取微信图片文件
	 * 
	 * @param access_token
	 * @param mediaid
	 * @return
	 */
	public static BufferedImage getImage(String access_token, String mediaid) {
		try {
			URL url = new URL("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + access_token + "&media_id=" + mediaid);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			BufferedImage image = ImageIO.read(inStream);
			inStream.close();
			return image;

		} catch (Exception e) {
			log.debug(e);
			return null;
		}
	}

	/**
	 * 获取微信视频文件
	 * 
	 * @param access_token
	 * @param mediaid
	 * @return
	 */
	public static ByteArrayOutputStream getVideo(String access_token, String mediaid) {
		try {
			URL url = new URL("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + access_token + "&media_id=" + mediaid);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inStream.read(buffer)) != -1) {
				outstream.write(buffer, 0, len);
			}
			outstream.close();
			inStream.close();
			return outstream;
		} catch (Exception e) {
			log.debug(e);

			return null;
		}
	}
}
