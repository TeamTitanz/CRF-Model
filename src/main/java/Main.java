import java.io.IOException;

public class Main {

    public static void main(String [] args) {
        String readText = "";
        CorpusBuilder corpusBuilder = new CorpusBuilder();
        try {
            readText = corpusBuilder.readFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        corpusBuilder.createWordList(readText);
        try {
            readText = corpusBuilder.readFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
