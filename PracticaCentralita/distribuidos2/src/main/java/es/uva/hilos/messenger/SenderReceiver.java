package es.uva.hilos.messenger;

import java.util.Queue;

public abstract class SenderReceiver<T> extends Thread {
    private Queue<Message<T>> inputQueue;  // Cola para recibir mensajes
    private Queue<Message<T>> outputQueue; // Cola para enviar mensajes

    // Establecer las colas de entrada y salida para este hilo (las maneja el manager)
    public SenderReceiver(Queue<Message<T>> inputQueue, Queue<Message<T>> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    // Método abstracto para procesar el mensaje
    protected abstract Message<T> process(Message<T> message);

    // Método para enviar un mensaje a través de la cola de salida
    public void sendMessage(Message<T> message) {
        outputQueue.add(message);  // Agregar mensaje a la cola de salida
    }

    // Método para recibir un mensaje desde la cola de entrada
    public Message<T> receiveMessage() {
        return inputQueue.poll();  // Recuperar el mensaje de la cola de entrada
    }

    @Override
    public void run() {
        while (true) {
            Message<T> message = receiveMessage();
            if (message != null) {
                System.out.println(this.getName() + " received: " + message.getContent());

                // Procesar el mensaje con el método abstracto process
                Message<T> response = process(message);

                // Enviar la respuesta si existe
                if (response != null) {
                    sendMessage(response);
                }
            }
        }
    }
}
