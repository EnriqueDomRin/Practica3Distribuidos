package es.uva.hilos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Empleado {
	private final static Logger logger = LoggerFactory.getLogger(Empleado.class);
	private final int prioridad;
	private final String nombre;
	final AtomicBoolean disponible = new AtomicBoolean(true);

	public Empleado(int prioridad, String nombre) {
		this.prioridad = prioridad;
		this.nombre = nombre;
	}

	public int getPrioridad() {
		return prioridad;
	}


	public void atenderLlamada(Llamada llamada) throws InterruptedException {
		logger.info(nombre + " está atendiendo la llamada " + llamada.getId() + "...");
		TimeUnit.SECONDS.sleep(llamada.getDuracion());
		logger.info(nombre + " atendió la llamada " + llamada.getId());
		Centralita centralita = new Centralita();
		centralita.liberar(this);
	}
}
