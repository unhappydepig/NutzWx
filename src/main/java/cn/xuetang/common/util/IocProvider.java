package cn.xuetang.common.util;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class IocProvider {
	private static final Log logger = Logs.get();

	private static Ioc ioc;

	public static void init()
	{
		if (ioc == null){
			try {
				ioc = new NutIoc(new ComboIocLoader("*org.nutz.ioc.loader.json.JsonLoader","config/datasource.js"));
			} catch (ClassNotFoundException e) {
				logger.error("Ioc create error", e);
			}
		}
	}
	public static Ioc ioc() {
		if (ioc == null) {
			throw Lang.makeThrow("ioc is null");
		}
		return ioc;
	}
}
