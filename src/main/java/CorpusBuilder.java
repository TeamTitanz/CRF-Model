import com.sun.tools.corba.se.idl.constExpr.Equal;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

public class CorpusBuilder {

    public String readFiles() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("CaseSet.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public void createWordList(String document) {
        // Create a document. No computation is done yet.
        Document doc = new Document(document);

        try {
            PrintWriter writer = new PrintWriter("train.txt", "UTF-8");
            for (Sentence sent : doc.sentences()) {  // Will iterate over sentences

                Properties props = new Properties();

                props.setProperty("annotators","tokenize, ssplit, pos, lemma");

                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                Annotation annotation = new Annotation(sent.text());
                pipeline.annotate(annotation);
                List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

                for (CoreMap sentence : sentences) {
                    for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        // this is the POS tag of the token
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        //this is the lemmatized word
                        String lemma =  token.get(CoreAnnotations.LemmaAnnotation.class);
                        //check for first letter capital
                        int isFirstLetterCapital = 0;
                        if (Character.isUpperCase(word.charAt(0))){
                            isFirstLetterCapital = 1;
                        }
                        // A feature representing the long word shape of the current token.
                        // This feature is defined by mapping any uppercase letter,
                        // lowercase letter, digit, and other characters to X x 0 and O respectively.
                        // For example, the long word shape of ”d-glycericacidemia” is xOxxxxxxxxxxxxxxxx.
                        String stringFeature_1 = "";
                        for (int i = 0, n = word.length(); i < n; i++) {
                            char c = word.charAt(i);
                            if (Character.isUpperCase(c)){
                                stringFeature_1 = stringFeature_1 + "X";
                            } else if (Character.isDigit(c)){
                                stringFeature_1 = stringFeature_1 + "0";

                            } else if (Character.isLetter(c)){
                                stringFeature_1 = stringFeature_1 + "x";

                            } else {
                                stringFeature_1 = stringFeature_1 + "O";

                            }
                        }
                        //A feature representing the brief word class of the current token.
                        // In this feature, consecutive uppercase letters, lowercase letters,
                        // digits, and other characters map to X, x, 0, and O, respectively.
                        // For example, the brief word shape of ”d-glycericacidemia” is xOx.
                        String stringFeature_2 = "";
                        for (int i = 0, n = word.length(); i < n; i++) {
                            char c = word.charAt(i);
                            if (i != 0){
                                char cPrevious = word.charAt(i-1);
                                if (Character.isUpperCase(c) && !Character.isUpperCase(cPrevious)){
                                    stringFeature_2 = stringFeature_2 + "X";
                                } else if (Character.isDigit(c) && !Character.isDigit(cPrevious)){
                                    stringFeature_2 = stringFeature_2 + "0";
                                } else if (Character.isLowerCase(c) && !Character.isLowerCase(cPrevious)){
                                    stringFeature_2 = stringFeature_2 + "x";
                                } else if (isPunctuation(c) && !isPunctuation(cPrevious)){
                                    stringFeature_2 = stringFeature_2 + "O";

                                }
                            } else {
                                if (Character.isUpperCase(c)) {
                                    stringFeature_2 = stringFeature_2 + "X";
                                } else if (Character.isDigit(c)) {
                                    stringFeature_2 = stringFeature_2 + "0";

                                } else if (Character.isLetter(c)) {
                                    stringFeature_2 = stringFeature_2 + "x";

                                } else {
                                    stringFeature_2 = stringFeature_2 + "O";

                                }
                            }
                        }

                        // A feature representing the type of token: word, number, symbol or punctuation.
                        // 1- word, 2- number, 3 - symbol or punctuation
                        String stringFeature_3 = "";
                        if (StringUtils.isNumeric(word)) {
                            stringFeature_3 = "2";
                        } else if (StringUtils.isAlpha(word)){
                            stringFeature_3 = "1";
                        } else {
                            stringFeature_3 = "3";
                        }

                        //write data tuple to file
                        writer.println(word + " " + pos + " " + lemma + " " + word + " " +
                                isFirstLetterCapital + " " + stringFeature_1 + " " + stringFeature_2 + " " + stringFeature_3);
                    }
                }
                writer.println();

            }
            writer.close();
        } catch (IOException e){
            System.out.println("Exception in writing file.");
        }
    }

    public static boolean isPunctuation(char c) {
        return c == ','
                || c == '.'
                || c == '!'
                || c == '?'
                || c == ':'
                || c == ';'
                || c == '-'
                || c == '_'
                || c == '+'
                || c == '='
                || c == '@'
                || c == '#'
                || c == '$'
                || c == '%'
                || c == '^'
                || c == '&'
                || c == '*'
                || c == '('
                || c == ')'
                || c == '{'
                || c == '}'
                || c == '['
                || c == ']'
                || c == '\\'
                || c == '|'
                || c == ':'
                || c == ';'
                || c == '\''
                || c == '"'
                || c == '<'
                || c == ','
                || c == '>'
                || c == '/'

                ;
    }



}
