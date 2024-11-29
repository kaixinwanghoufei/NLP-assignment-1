import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class TextProcessor {
    public static void main(String[] args) throws IOException {
        // file path
        String inputFile = "alice29.txt";
        String cleanedFile = "cleaned.txt";
        String sentencesFile = "sentences.txt";
        String wordsFile = "words.txt";
        String top10WordsFile = "top10words.txt";

        // Step 1 clean
        String text = new String(Files.readAllBytes(Paths.get(inputFile)));
        String cleanedText = text.replaceAll("[^a-zA-Z\\.\\!\\?\\s]", "").toLowerCase();
        Files.write(Paths.get(cleanedFile), cleanedText.getBytes());

        // Step 2 sentences
        String[] sentences = cleanedText.split("[\\.\\!\\?]");
        Files.write(Paths.get(sentencesFile), Arrays.asList(sentences));

        // Step 3 words
        List<String> words = Arrays.asList(cleanedText.split("\\s+"));
        Files.write(Paths.get(wordsFile), words.stream().collect(Collectors.joining("\n")).getBytes());

        // Step 4
        Map<String, Long> wordCounts = words.stream()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        List<Map.Entry<String, Long>> top10Words = wordCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .collect(Collectors.toList());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(top10WordsFile))) {
            for (Map.Entry<String, Long> entry : top10Words) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        }


        System.out.println("Top 10 Most Frequent Words:");
        System.out.printf("%-15s %s%n", "Word", "Frequency");
        System.out.println("---------------------------");
        for (Map.Entry<String, Long> entry : top10Words) {
            System.out.printf("%-15s %d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("Processing complete. Output files created.");
    }
}
