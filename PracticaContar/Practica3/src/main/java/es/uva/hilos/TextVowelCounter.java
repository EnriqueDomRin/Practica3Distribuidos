package es.uva.hilos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TextVowelCounter {

    // Method that takes a String input and returns an ArrayList of words
    public static ArrayList<String> getWords(String input) {
        // TODO
        // Divide el texto en palabras usando espacios como delimitador. El método devolverá una lista de palabras
        ArrayList<String> words = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            words.addAll(Arrays.asList(input.split("\\s+"))); // Split by whitespace
        }
        return words;
    }

    // Method that counts vowels in a string using parallelism
        // Method that counts vowels in a string using parallelism
    public static int getVowels(String input, int parallelism) throws InterruptedException {

        // Create queues
        BlockingQueue<String> wordQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Result> resultQueue = new LinkedBlockingQueue<>();

        // Array para almacenar las palabras
        ArrayList<String> words = getWords(input);
        // Añaadir todas las palabras a la cola de palabras
        wordQueue.addAll(words);

        // Create and start the worker threads based on the parallelism parameter
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < parallelism; i++) {
            WordVowelCounter worker = new WordVowelCounter(wordQueue, resultQueue);
            Thread workerThread = new Thread(worker);
            workers.add(workerThread);
            workerThread.start();  // Start each worker thread
        }

        // Wait for all worker threads to finish
        for (Thread worker : workers) {
            worker.join();
        }

        // Gather results from resultQueue
        // TODO
        // Extrae los resultados de resultQueue y suma el número total de vocales en todas las palabras.
        int totalVowelCount = 0;
        Result result;
        // Continuar hasta que la cola esté vacía o el hilo se interrumpa
        while ((result = resultQueue.poll(1, TimeUnit.SECONDS)) != null) {
            totalVowelCount += result.getVowelCount();
        }

        return totalVowelCount;
    }
}