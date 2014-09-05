package cn.xuetang.common.util;

import java.io.BufferedReader;
import java.sql.ResultSet;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.xuetang.modules.user.bean.PermissionCategory;
 

/**
 * @author Wizzer.cn
 * @time   2012-9-13 下午6:13:55
 *
 */
public class DBObject {
	private static final Log log = Logs.get();
	/*
	 * 获取CLOB字段值
	 */
	public static String getClobBody(ResultSet rs, String colnumName)
	{
		String result = "";
		try
		{
			String str_Clob = "";
			StringBuffer strBuffer_CLob = new StringBuffer();
			strBuffer_CLob.append("");
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(colnumName);
			BufferedReader in = new BufferedReader(clob.getCharacterStream());
			while ((str_Clob = in.readLine()) != null)
			{
				strBuffer_CLob.append(str_Clob + "\n");
			}
			in.close();
			result = strBuffer_CLob.toString();
		}
		catch (Exception e)
		{
			log.debug(e.getMessage());
		}
		return result;
	}
	public static void main(String[] args) {
		IocProvider.init();
		Ioc ioc = IocProvider.ioc();
		Dao dao = ioc.get(Dao.class);
		PermissionCategory pc = dao.fetch(PermissionCategory.class, Cnd.where("id", "=", "d9bd8fa13be8421e9dc038d61b0a2803"));
		dao.fetchLinks(pc, "parent");
	}
}


