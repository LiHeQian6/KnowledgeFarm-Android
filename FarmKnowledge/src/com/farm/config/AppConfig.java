package com.farm.config;

import com.farm.config.routes.AdminRoutes;
import com.farm.config.routes.FrontRoutes;
import com.farm.model.Admin;
import com.farm.model.Crop;
import com.farm.model.User;
import com.farm.model.UserAuthority;
import com.farm.model.UserBag;
import com.farm.model.UserCrop;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

public class AppConfig extends JFinalConfig {

	@Override
	public void onStart() {
		System.out.println("start");
	}

	@Override
	public void onStop() {
		System.out.println("stop");
	}

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true); //开发者模式
		me.setViewType(ViewType.JSP); 
		me.setError404View("/404.jsp"); 

	}

	@Override
	public void configRoute(Routes me) {
		me.add(new FrontRoutes()); //前端
        me.add(new AdminRoutes()); //后端

	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost:3306/farm_db?useSSL=true&useUnicode=true&characterEncoding=utf8", "root", "");
		me.add(dp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		me.add(arp);
		arp.setDialect(new MysqlDialect());
		arp.addMapping("user", User.class);
		arp.addMapping("userAuthority", UserAuthority.class);
		arp.addMapping("admin", Admin.class);
		arp.addMapping("crop", Crop.class);
		arp.addMapping("userbag", UserBag.class);
		arp.addMapping("usercrop", UserCrop.class);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

}
