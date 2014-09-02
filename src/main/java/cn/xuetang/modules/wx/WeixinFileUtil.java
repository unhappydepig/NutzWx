package cn.xuetang.modules.wx;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import cn.xuetang.service.AppInfoService;

/**
 * Created by Wizzer on 14-4-29.
 */
@IocBean
@At("/private/wx/file")
public class WeixinFileUtil{
	@Inject
	protected UploadAdaptor upload;

	@Inject
	private AppInfoService appInfoService;
	@At
	@Ok("raw")
	@AdaptBy(type = UploadAdaptor.class, args = "ioc:upload")
	public String upload(@Param("Filedata") TempFile tmpFile, @Param("appid") int appid, @Param("type") String type, AdaptorErrorContext errCtx) {
		Map<String, Object> js = new HashMap<String, Object>();
		if (errCtx != null) {
			if (errCtx.getAdaptorErr() != null) {
				js.put("error", errorMsg(errCtx.getAdaptorErr()));
				js.put("msg", "");
				return Json.toJson(js);
			}
			for (Throwable e : errCtx.getErrors()) {
				if (e != null) {
					js.put("error", errorMsg(e));
					js.put("msg", "");
					return Json.toJson(js);
				}
			}
		}
		try {
			URL urlObj = new URL("http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + appInfoService.getGloalsAccessToken(appid) + "&type=" + type);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			/**
			 * 设置关键值
			 */
			conn.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false); // post方式不能使用缓存
			// 设置请求头信息
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			// 设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			// 请求正文信息

			// 第一部分：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // ////////必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + tmpFile.getFile().getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(head);
			// 文件正文部分
			DataInputStream in = new DataInputStream(new FileInputStream(tmpFile.getFile()));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			/**
			 * 读取服务器响应，必须读取,否则提交不成功
			 */
			int res = conn.getResponseCode();
			if (res == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = null;
				StringBuffer msg = new StringBuffer();
				if ((line = reader.readLine()) != null) {
					msg.append(line).append("\n");
				}
				reader.close();
				conn.disconnect();
				return msg.toString();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				String line = null;
				StringBuffer errorMsg = new StringBuffer();
				if ((line = reader.readLine()) != null) {
					errorMsg.append(line).append("\n");
				}
				reader.close();
				conn.disconnect();
				return errorMsg.toString();
			}
		} catch (IOException e) {
			js.put("error", "IO errer");
			js.put("msg", "");
			return Json.toJson(js);
		}
	}

	/**
	 * 根据异常提示错误信息
	 * 
	 * @param t
	 * @return
	 */
	private String errorMsg(Throwable t) {
		if (t == null || t.getClass() == null) {
			return "错误：未知system错误！";
		} else {
			String className = t.getClass().getSimpleName();
			if (className.equals("UploadUnsupportedFileNameException")) {
				String name = upload.getContext().getNameFilter();
				return "错误：无效的文件扩展名，支持的扩展名：" + name.substring(name.indexOf("(") + 1, name.lastIndexOf(")")).replace("|", ",");
			} else if (className.equals("UploadUnsupportedFileTypeException")) {
				return "错误：不支持的文件类型！";
			} else if (className.equals("UploadOutOfSizeException")) {
				return "错误：文件超出限制大小！";
			} else if (className.equals("UploadStopException")) {
				return "错误：上传中断！";
			} else {
				return "错误：未知错误！";
			}
		}
	}
}
