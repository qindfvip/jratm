package com.base.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.base.common.utils.SystemOut;
import com.base.common.utils.xx;
import com.base.common.utils.db.MysqlUtil;
import com.base.interceptor.AuthInterceptor;
import com.base.interceptor.LoginInterceptor;
import com.base.model.Button;
import com.base.model.Menu;
import com.base.model.MenuObject;
import com.base.model.MetaItem;
import com.base.model.MetaObject;
import com.base.model.Role;
import com.base.model.RoleBtn;
import com.base.model.SystemLog;
import com.base.model.User;
import com.base.service.ServiceManager;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;

public class AppConfig extends JFinalConfig {

	private long startTime = 0;

	/**
	 * 系统启动之后
	 */
	@Override
	public void afterJFinalStart() {
		System.err.println("JFinal Started\n");
		// Load Cost Time
		costTime(startTime);
	}

	/**
	 * 系统停止之前
	 */
	@Override
	public void beforeJFinalStop() {
	}

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		startTime = System.currentTimeMillis();

		System.out.println("Config Constants Starting...");
		// 加载配置文件
		loadPropertyFile("jdbc.properties");
		// 开发模式
		me.setDevMode(getPropertyToBoolean("devMode", true));
		// 默认主视图
		// me.setViewType(ViewType.FREE_MARKER);
		// 设置主视图为Beetl
		me.setMainRenderFactory(new BeetlRenderFactory());
		// POST内容最大500M(安装包上传)
		me.setMaxPostSize(1024 * 1024 * 500);

		@SuppressWarnings("unused")
		GroupTemplate group = BeetlRenderFactory.groupTemplate;

		// 注册函数
		// group.registerFunction("isTrue", new IsTrueFun());
		// group.registerFunction("format", new JsFormatFun());

		// 设置全局变量
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		String CDN = getProperty("domain_cdn", "http://127.0.0.1");
		sharedVars.put("CDN", CDN);
		sharedVars.put("ADMIN_PATH", AppConst.ADMIN_PATH);

		// Load Template Const
		PageConst.init(sharedVars);

		BeetlRenderFactory.groupTemplate.setSharedVars(sharedVars);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		SystemOut.println("Config Routes Starting...");
		
		me.add(AdminRoutes.me());
		me.add(FrontRoutes.me());

		addRoutes(me, "com.jratm.app.controller", "atm");
		
		Set<Entry<String, Class<? extends Controller>>> pathUrlSet = me.getEntrySet();
		Iterator<Entry<String, Class<? extends Controller>>> pathUrlIt = pathUrlSet.iterator();
		while(pathUrlIt.hasNext()){
			Entry<String, Class<? extends Controller>> entry = pathUrlIt.next();
			System.out.println("Config Routes url..." + entry.getKey());
		}

	}

	/**
	 * 
	 * @param me:Routes
	 * @param path:扫描包路径
	 * @param prefixViewPath:Web资源路径前缀
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRoutes(Routes me, String path, String prefixViewPath) {
		List<Class> list = ClassSearcher.findClasses(path);
		if (list != null && list.isEmpty() == false) {
			for (Class clz : list) {
				RouteBind rb = (RouteBind) clz.getAnnotation(RouteBind.class);
				if (rb != null) {
					String clzDir = (new File(clz.getResource("").getPath())).getName();
					if ("ctrl".equals(clzDir)) {
						clzDir = "";
					} else {
						clzDir += "/";
					}
					String route = rb.path();
					SystemOut.println("Add Route: " + route);
					me.add(route, clz, (StringUtils.isBlank(rb.viewPath()) ? prefixViewPath : AppConst.PREFIX_VIEW_PATH) + rb.viewPath());
				}
			}
		}
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		SystemOut.println("Config Plugins Starting...");

		String url, user, pwd;
		if (isLocal()) {
			// 本地环境
			url = getProperty("local_url");
			user = getProperty("local_user");
			pwd = getProperty("local_pwd");

		
		} else {
			// 正式环境
			url = getProperty("url");
			user = getProperty("user");
			pwd = getProperty("pwd");

		}

		// 设置Mysql方言
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");

		// DruidPlugin Default DataSource
		DruidPlugin main = new DruidPlugin(url, user, pwd);
		main.addFilter(new StatFilter());
		main.addFilter(wall);
		me.add(main);

		// 配置ActiveRecord插件(默认主数据)  添加自动绑定model与表插件
		 ActiveRecordPlugin arp = new ActiveRecordPlugin(xx.DS_MAIN, main);
		 arp.setShowSql(true);
		 me.add(arp);
		 autoAddMapping(arp);

		// 记录数据库名称
		AppConst.DBMAP.put(xx.DS_MAIN, MysqlUtil.getDbNameByUrl(url));
		SystemOut.println("load main datasource:" + url + "/" + user + "/" + pwd);

		// DruidPlugin base DataSource
		DruidPlugin base = new DruidPlugin(url, user, pwd);
		base.addFilter(new StatFilter());
		base.addFilter(wall);
		me.add(base);

		// 配置ActiveRecord插件
		ActiveRecordPlugin base_arp = new ActiveRecordPlugin(xx.DS_BASE, base);
		base_arp.setShowSql(true);
		base_arp.addMapping("eova_object", MetaObject.class);
		base_arp.addMapping("eova_item", MetaItem.class);
		base_arp.addMapping("eova_button", Button.class);
		base_arp.addMapping("eova_user", User.class);
		base_arp.addMapping("eova_menu", Menu.class);
		base_arp.addMapping("eova_menu_object", MenuObject.class);
		base_arp.addMapping("eova_role", Role.class);
		base_arp.addMapping("eova_role_btn", RoleBtn.class);
		base_arp.addMapping("eova_log", SystemLog.class);
		me.add(base_arp);

		// 记录数据库名称
		AppConst.DBMAP.put(xx.DS_BASE, MysqlUtil.getDbNameByUrl(url));
		SystemOut.println("load  datasource:" + url + "/" + user + "/" + pwd);

		// 初始化ServiceManager
		ServiceManager.init();

		// 配置EhCachePlugin插件
		me.add(new EhCachePlugin());

		/** spring 配置插件 */
		// SpringPlugin springplugin = new SpringPlugin();
		// me.add(springplugin);
		
		// me.add(new SqlInXmlPlugin());

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void autoAddMapping(ActiveRecordPlugin arp){
		List<Class> modelClasses = ClassSearcher.findClasses(Model.class);
		TableBind tb = null;
		for (Class modelClass : modelClasses) {
			tb = (TableBind) modelClass.getAnnotation(TableBind.class);
			if (tb == null) {
				continue;
			} else {
				if (StrKit.notBlank(tb.name())) {
					if (StrKit.notBlank(tb.pk())) {
						arp.addMapping(tb.name(), tb.pk(), modelClass);
					} else {
						arp.addMapping(tb.name(), modelClass);
					}
				}
			}
		}
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		System.err.println("Config Interceptors Starting...");
		// JFinal.me().getServletContext().setAttribute("KING", "test");
		// 登录验证
		me.add(new LoginInterceptor());
		// 权限验证拦截
		me.add(new AuthInterceptor());
	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		System.err.println("Config Handlers Starting...");
		// 添加DruidHandler
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
		me.add(dvh);
	}

	/**
	 * 是否本地环境
	 * 
	 * @return
	 */
	private boolean isLocal() {
		String osName = System.getProperty("os.name");
		if (osName.indexOf("Windows") != -1 || osName.indexOf("Mac") != -1)
			return true;
		else
			return false;
	}

	private void costTime(long time) {
		System.err.println("Load Cost Time:" + (System.currentTimeMillis() - time) + "ms\n");
	}
}