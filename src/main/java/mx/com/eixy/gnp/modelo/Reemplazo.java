package mx.com.eixy.gnp.modelo;

public class Reemplazo {

    private String cadenaActual;
    private String cadenaNueva;
    private String condicionBusqueda;

    public String getCadenaActual() {
	return cadenaActual;
    }

    public void setCadenaActual(final String cadenaActual) {
	this.cadenaActual = cadenaActual;
    }

    public String getCadenaNueva() {
	return cadenaNueva;
    }

    public void setCadenaNueva(String cadenaNueva) {
	this.cadenaNueva = cadenaNueva;
    }

    public String getCondicionBusqueda() {
	return condicionBusqueda;
    }

    public void setCondicionBusqueda(String condicionBusqueda) {
	this.condicionBusqueda = condicionBusqueda;
    }

    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return "Busca " + condicionBusqueda + " cambia " + cadenaActual
		+ " por " + cadenaNueva;
    }

}
