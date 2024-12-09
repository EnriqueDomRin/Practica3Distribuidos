package es.uva.hilos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Centralita {
	// Desde a la centralita pueden llegar llamadas que despues
	// se asignan a los empleados disponibles según prioridad



	// TODO: Harán falta más atriubutos y métodos para implementar la centralita

	// ExecutorService para gestionar los hilos de manera asíncrona
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	private final ArrayList<Empleado> empleados = new ArrayList<>();

	// Cola de llamadas en espera
	private final LinkedBlockingQueue<Llamada> colaLlamadas = new LinkedBlockingQueue<>();

	public void conEmpleado(Empleado e) {
		empleados.add(e);
	}

	public Runnable atenderLlamadaConEmpleado(Empleado empleado, Llamada llamada){
		// TODO: Obligatorio devolver un Runnable. Se recomienda utilzar
		// funciones llamada.
		return () -> {
			try {
				// El empleado atiende la llamada
				empleado.atenderLlamada(llamada);
				// Procesar la siguiente llamada en la cola si existe
				procesarColaLlamadas();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};

	}

	public void atenderLlamada(Llamada llamada){
		// TODO: Este método debería seleccionar un empleado disponible según prioridad
		// y correr en un nuevo hilo atenderLlamadaConEmpleado.
		// Este método no bloquea la ejecución si hay empleados disponibles para atender la llamada
		// si no hay empleados disponibles tendremos que esperar a que haya uno.

		// Buscar un empleado disponible según prioridad
		Optional<Empleado> empleadoOpt = empleados.stream()
				.sorted(Comparator.comparingInt(Empleado::getPrioridad))
				.filter(this::estaDisponible)
				.findFirst();

		if (empleadoOpt.isPresent()) {
			// Si hay un empleado disponible, lo ocupa y le asigna la llamada
			Empleado empleado = empleadoOpt.get();
			ocupar(empleado);
			executorService.submit(atenderLlamadaConEmpleado(empleado, llamada));
		} else {
			// Si no hay empleados disponibles, añade la llamada a la cola
			colaLlamadas.offer(llamada);
		}
	}

	// Método para verificar si el empleado está disponible
	public boolean estaDisponible(Empleado empleado) {
		return empleado.disponible.get();
	}

	// Método para marcar al empleado como ocupado
	public void ocupar(Empleado empleado) {
		empleado.disponible.set(false);
	}

	// Método para liberar al empleado después de atender una llamada
	public void liberar(Empleado empleado) {
		empleado.disponible.set(true);
	}

	// Método para procesar la siguiente llamada en la cola
	private void procesarColaLlamadas() {
		Llamada llamada = colaLlamadas.poll();
		if (llamada != null) {
			atenderLlamada(llamada);
		}
	}

}
