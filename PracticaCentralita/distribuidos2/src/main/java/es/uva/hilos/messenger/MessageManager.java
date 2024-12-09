package es.uva.hilos.messenger;

import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiFunction;

public class MessageManager<T,SR extends SenderReceiver<T>> {
    // Map que almacena las colas de entrada y salida de los hilos registrados
    private final Map<String, Queue<Message<T>>> inputQueues = new ConcurrentHashMap<>();
    private final Map<String, Queue<Message<T>>> outputQueues = new ConcurrentHashMap<>();
    private final ArrayList<SR> srs = new ArrayList<>();
    private final BiFunction<Queue<Message<T>>,Queue<Message<T>>,SR> factory;

    public MessageManager(BiFunction<Queue<Message<T>>,Queue<Message<T>>,SR> factory){
        this.factory = factory;
    }

    // Registrar un nuevo participante (hilo)
    public SenderReceiver<T> registerParticipant(String threadName) {
        Queue<Message<T>> inputQueue = new LinkedBlockingQueue<>();
        Queue<Message<T>> outputQueue = new LinkedBlockingQueue<>();
        inputQueues.put(threadName, inputQueue);
        outputQueues.put(threadName, outputQueue);
        SR sr = factory.apply(inputQueue, outputQueue); // Crear un hilo con las colas asignadas
        sr.start();
        srs.add(sr);
        return sr;
    }

    public void endAll(){
        for (Thread sr : srs){
            sr.interrupt();
        }
    }

    public void sendMessage(Message<T> msg){
        Queue<Message<T>> destinataryQueue = inputQueues.get(msg.getDestinatary());
        if (destinataryQueue != null) {
            destinataryQueue.add(msg);  // Enviar mensaje a la cola del destinatario
        } else {
            System.out.println("Destinatario no encontrado: " + msg.getDestinatary());
        }
    }

    // Lee de las colas de salida y enrutarlas a las colas de entrada correspondientes
    public void processMessages() throws InterruptedException {
        for (Map.Entry<String, Queue<Message<T>>> entry : outputQueues.entrySet()) {
            Queue<Message<T>> outputQueue = entry.getValue();
            Message<T> message = outputQueue.poll(); // Leer mensaje de la cola de salida
            if (message != null) {
                sendMessage(message);
            }
        }
    }
}
