package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering {

    private TransactionAnalyzer transactionAnalyzer;
    private static TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;


    public MoneyLaundering() {

        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public void processTransactionData(){

        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        for(File transactionFile : transactionFiles) {
            int threads = 5;
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            ControllerThreads transaction[] = new ControllerThreads[threads];
            int start = 0;
            int end;
            int arreglo = transaction.length/threads;
            for (int i = 0; i < threads; i++) {
                end = start+arreglo;
                transaction[i] = new ControllerThreads(start++,end,transactions);
                transaction[i].start();
                start=end;
            }
        }
    }

    public List<String> getOffendingAccounts(){

        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList(){

        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args){

        int threads = 5;
        AtomicInteger counts = new AtomicInteger(threads);
        System.out.println(getBanner());
        System.out.println(getHelp());

        MoneyLaundering moneyLaundering = new MoneyLaundering();
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        while(counts.get()>0){
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
            {
                System.exit(0);
            }

            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
        }
    }

    private static String getBanner(){
        String banner = "\n";
        try {
            banner = String.join("\n", Files.readAllLines(Paths.get("src/main/resources/banner.ascii")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return banner;
    }

    private static String getHelp() {

        String help = "Type 'exit' to exit the program. Press 'Enter' to get a status update\n";
        return help;
    }

    class ControllerThreads extends Thread{
        private List<Transaction> transactions;
        private int start;
        private int end;

        public ControllerThreads(int start, int end, List<Transaction> transactions){
            this.transactions=transactions;
            this.start=start;
            this.end=end;
        }
        @Override
        public void run() {
            synchronized (amountOfFilesProcessed) {
                for (int i = start; i < end; i++) {
                    transactionAnalyzer.addTransaction(transactions.get(i));
                }
                amountOfFilesProcessed.incrementAndGet();
            }
        }
    }
}