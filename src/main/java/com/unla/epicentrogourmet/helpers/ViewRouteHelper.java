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
	public final static String EMPLEADO_BAJAS = "responsable/empleados/bajas";
	public final static String CAJA_HISTORIAL = "empleado/cajas/historial";

	//UNIDAD-VENTA
	public final static String UNIDADVENTA_NEW = "unidadventa/new";

	//PLATO (Responsable)
	public final static String PLATO_INDEX = "plato/index";
	public final static String PLATO_NEW = "plato/new";
	public final static String PLATO_EDIT = "plato/edit";
	public final static String PLATO_BAJAS = "plato/bajas";
	public final static String PLATO_ESTRELLA = "plato/estrella";

	//RENTABILIDAD (Responsable)
	public final static String RENTABILIDAD_INDEX = "rentabilidad/index";
	public final static String RENTABILIDAD_PLATOS = "rentabilidad/platos";
	public final static String RENTABILIDAD_VENTAS = "rentabilidad/ventas";

	//PEDIDOS (empleado)
	public final static String PEDIDO_FORM = "empleado/pedidos/form";

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
	public final static String EMPLEADOS_ROOT = "/responsable/empleados";
	public final static String PEDIDO_NEW = "/empleado/pedidos/new";
}
