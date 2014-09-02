package cn.xuetang.modules.wx;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

/**
 * Created by Wizzer on 14-8-11.
 */
@IocBean(fields = "wxHandler")
public class WxModule {
	protected WxHandler wxHandler;

	public WxModule() {
		Wxs.enableDevMode(); // 开启debug模式,这样就会把接收和发送的内容统统打印,方便查看
	}

	@At({ "/weixin", "/weixin/?" })
	@Fail("http:200")
	public View msgIn(String mykey, HttpServletRequest req) throws IOException {
		return Wxs.handle(getWxHandler(mykey), req, mykey);
	}

	public WxHandler getWxHandler(String key) {
		return wxHandler;
	}

}
