package com.farm.config.routes;

import com.farm.admin.controller.AdminController;
import com.farm.admin.controller.CropController;
import com.farm.admin.controller.UserController;
import com.farm.config.AdminInterceptor;
import com.jfinal.config.Routes;

public class AdminRoutes extends Routes {
    @Override
    public void config() {
    	addInterceptor(new AdminInterceptor());
    	
    	add("/",AdminController.class);
        add("/admin",AdminController.class);
        add("/admin/user",UserController.class);
        add("/admin/crop",CropController.class);
    }
}
