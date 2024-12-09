package es.uva.hilos.messenger;

// EL mensaje puede contener cualquier tipo de dato
public class Message<T> {
    private final T content;
    private final String destinatary; // Tiene un destinatario

    public Message(T content, String destinatary) {
        this.content = content;
        this.destinatary = destinatary;
    }

    public T getContent() {
        return content;
    }

    public String getDestinatary() {
        return destinatary;
    }
}
