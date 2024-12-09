package es.uva.hilos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class WordVowelCounter implements Runnable {
    private BlockingQueue<String> wordQueue;
    private BlockingQueue<Result> resultQueue;

    // Constructor
    public WordVowelCounter(BlockingQueue<String> wordQueue, BlockingQueue<Result> resultQueue) {
        this.wordQueue = wordQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        // TODO
        // Extrae palabras de wordQueue, cuenta las vocales de cada palabra y almacena los resultados en resultQueue.
        // Se hara en un ciclo continuo hasta que wordQueue esté vacía.
        try {
            String word;
            // Continuar hasta que la cola esté vacía o el hilo se interrumpa
            while ((word = wordQueue.poll(1, TimeUnit.SECONDS)) != null) {
                int vowelCount = countVowels(word); // Contar las vocales
                resultQueue.put(new Result(word, vowelCount)); // Almacenar el resultado
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Manejar la interrupción del hilo
        }
    }

    private int countVowels(String word) {
        // Cuenta las vocales en una palabra. Para hacerlo, recorreremos cada carácter de la
        // palabra y verificaremos si es una vocal (tanto minúsculas como mayúsculas).
        int count = 0;
        String vowels = "aeiouAEIOU";
        for (char c : word.toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }
}
