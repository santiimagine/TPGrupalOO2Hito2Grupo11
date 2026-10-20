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
	public final static String EMPLEADO_LIST = "responsable/empleados/list";
	public final static String EMPLEADO_FORM = "responsable/empleados/form";

	//UNIDAD-VENTA
	public final static String UNIDADVENTA_NEW = "unidadventa/new";

	//USER
	public final static String USER_LOGIN = "user/login";
	public final static String USER_LOGOUT = "user/logout";

	/**** Redirects ****/
	public final static String ROUTE = "/index";
	public final static String ADMIN_ROOT = "/admin";
	public final static String RESPONSABLE_ROOT = "/responsable";
	public final static String EMPLEADO_ROOT = "/empleado";
	public final static String LOGIN_REGISTRO_OK = "/login?registro=ok";
	public final static String EMPLEADOS_ROOT = "/responsable/empleados";
}
