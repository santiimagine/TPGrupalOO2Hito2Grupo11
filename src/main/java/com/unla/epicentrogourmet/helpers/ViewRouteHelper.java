package com.unla.epicentrogourmet.helpers;

public class ViewRouteHelper {
	/**** Views ****/
	//HOME
	public final static String INDEX = "home/index";

	//ADMIN
	public final static String ADMIN_INDEX = "admin/index";

	//RESPONSABLE
	public final static String RESPONSABLE_INDEX = "responsable/index";

	//EMPLEADO
	public final static String EMPLEADO_INDEX = "empleado/index";

	//UNIDAD-VENTA
	public final static String UNIDADVENTA_NEW = "unidadventa/new";

	//PLATO (Responsable)
	public final static String PLATO_INDEX = "plato/index";
	public final static String PLATO_NEW = "plato/new";

	//USER
	public final static String USER_LOGIN = "user/login";
	public final static String USER_LOGOUT = "user/logout";

	/**** Redirects ****/
	public final static String ROUTE = "/index";
	public final static String ADMIN_ROOT = "/admin";
	public final static String RESPONSABLE_ROOT = "/responsable";
	public final static String EMPLEADO_ROOT = "/empleado";
	public final static String PLATO_ROOT = "/responsable/platos";
	public final static String LOGIN_REGISTRO_OK = "/login?registro=ok";
}
