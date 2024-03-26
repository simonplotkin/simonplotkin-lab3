import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WordCounter {

     public static int processText(StringBuffer text, String stopword) throws TooSmallText, InvalidStopwordException{
        int wordCounter = 0;
        int totalElements = 0;
        boolean stopwordChecker = true;

        Pattern regex = Pattern.compile("[a-zA-Z0-9']+"); 
        Matcher regexMatcher = regex.matcher(text);
        ArrayList<String> words = new ArrayList<String>();
        
        while (regexMatcher.find()) {
            words.add(regexMatcher.group());
            totalElements++;
        }

        if(words.size() < 5){
            throw new TooSmallText(totalElements);
        }

        for(String s : words){
            if(stopword != null){
                if(s.equals(stopword)){
                    stopwordChecker = false;
                    wordCounter++;
                    break;
                }
            }
            wordCounter++;
        }
        
        if(stopwordChecker && stopword != null){
            throw new InvalidStopwordException(stopword);
        } else {
            return wordCounter;
        }
     }

     
     public static StringBuffer processFile(String filepath) throws FileNotFoundException, IOException, EmptyFileException {
        boolean passed = false;
        String returnableString = "";
        while (!passed){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
                ArrayList<String> lines = new ArrayList<String>();
                String line = reader.readLine();

                
                if(line == null){
                    throw new EmptyFileException(filepath);
                } else {
                    passed = true;
                    while(line != null) {
                    lines.add(line);
                    returnableString += line;
                    line = reader.readLine();
                }
                }

            } catch (FileNotFoundException e) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Error: Cannot read file or file does not exist. Please input a new file name");
                filepath = sc.nextLine();

            }
        }
            StringBuffer s = new StringBuffer(returnableString);
            return s;
     }

     // TODO: MAIN METHOD
     public static void main(String[] args) throws Throwable {
        if(args.length == 0){
            System.out.println("WARNING: No command line arguments provided. Please provide your text or file and an optional stopword next time.");
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);
        int pChoice;
        System.out.println("Do you want to process a file (1) or process text (2)");
        StringBuffer countable;
        String stopword = null;
        int totalWordCount;
        

        // Check for if pChoice is either a 1 or a 2 and not anything else
        while(true){
            try {
                pChoice = sc.nextInt();

                if(pChoice == 1 || pChoice == 2){
                    break;
                    // sc.next();
                } else {
                    System.out.println("Please enter a 1 or 2 this time... : ");
                }

            } catch (Exception e) {
                System.out.println("Please enter a 1 or 2 this time... : ");
                sc.next();
            }
        }

        if(pChoice == 1){
            countable = WordCounter.processFile(args[0]);
        } else {
            countable = new StringBuffer(args[0]);
        }

        try{
            stopword = args[1];
        } catch(Exception e){
            stopword = null;
        }

        for(int i = 0; i < 2; i++){
            try {
                totalWordCount = WordCounter.processText(countable, stopword);

                System.out.println("SUCCESS. Your wordCount is: " + totalWordCount);
                break;
            } catch (InvalidStopwordException e) {
                if(i == 0){
                    Scanner scan = new Scanner(System.in);
                    System.out.println( e + ". You may try one more time to input a valid stop word. Otherwise, you will be reported.");
                    stopword = scan.nextLine();
                } else {
                    System.out.println(e + ". REPORTING USER");
                }
            } catch (TooSmallText e) {
                System.out.println(e);
                break;
            }
        }
        

        
        
     }
}