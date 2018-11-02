package mx.com.eixy.gnp.modelo;

import mx.com.eixy.swing.table.MyTableModel.Ignore;

public class Member {

	private String tipo;
	private String origen;
	private String destino;
	private String nombreOrigen;
	private String nombreDestino;

	@Ignore
	private String log = "";

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		if (this.log.isEmpty()) {
			this.log = log;
		}
	}

	public String getTipo() {
		return tipo;
	}

	public Member setTipo(String tipo) {
		this.tipo = tipo;
		return this;
	}

	public String getOrigen() {
		return origen;
	}

	public Member setOrigen(String origen) {
		this.origen = origen;
		return this;
	}

	public String getDestino() {
		return destino;
	}

	public Member setDestino(String destino) {
		this.destino = destino;
		return this;
	}

	public String getNombreOrigen() {
		return nombreOrigen;
	}

	public Member setNombreOrigen(String nombreOrigen) {
		this.nombreOrigen = nombreOrigen;
		return this;
	}

	public String getNombreDestino() {
		return nombreDestino;
	}

	public Member setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
		return this;
	}

}
