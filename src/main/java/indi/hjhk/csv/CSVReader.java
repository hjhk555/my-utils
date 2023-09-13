package indi.hjhk.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVReader {
    private interface StringConsumer{
        public void consume(String string, int index);
    }

    public static class CSVFormatException extends Exception{
        public CSVFormatException(String message) {
            super(message);
        }
    }

    private final String filePath;
    private HashMap<String, Integer> csvHeader;
    private ArrayList<String> currentRecord = new ArrayList<>();
    private BufferedReader fileReader;

    public CSVReader(String filePath) throws IOException {
        this.filePath = filePath;
        fileReader = Files.newBufferedReader(Paths.get(filePath));
    }

    public boolean readHeader() throws CSVFormatException, IOException {
        if (csvHeader != null){
            System.err.println("file header has been read");
            return false;
        }

        csvHeader = new HashMap<>();
        return consumeNextLine((string, index) -> {
            csvHeader.put(string, index);
        });
    }

    public boolean next(){
        currentRecord.clear();
        try {
            return consumeNextLine((string, index) -> {
                currentRecord.add(index, string);
            });
        } catch (CSVFormatException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getString(String columnName){
        if (csvHeader == null) {
            throw new NullPointerException("header not read");
        }

        return currentRecord.get(csvHeader.get(columnName));
    }

    public String getString(int index){
        return currentRecord.get(index);
    }

    public Long getLong(String columnName){
        if (csvHeader == null) {
            throw new NullPointerException("header not read");
        }

        return Long.parseLong(currentRecord.get(csvHeader.get(columnName)));
    }

    public Long getLong(int index){
        return Long.parseLong(currentRecord.get(index));
    }

    public Double getDouble(String columnName){
        if (csvHeader == null) {
            throw new NullPointerException("header not read");
        }

        return Double.parseDouble(currentRecord.get(csvHeader.get(columnName)));
    }

    public Double getDouble(int index){
        return Double.parseDouble(currentRecord.get(index));
    }

    private boolean consumeNextLine(StringConsumer consumer) throws IOException, CSVFormatException {
        StringBuilder strBuilder = new StringBuilder();
        int nextCh = fileReader.read();
        if (nextCh == -1) {
            // tail reached
            return false;
        }

        int index = 0;
        // ignore empty line
        while (nextCh == '\n'){
            nextCh = fileReader.read();
            if (nextCh == -1)
                return false;
        }
        // read line
        while(true){
            // get next string
            if (nextCh == '"') {
                // surrounding double quote
                nextCh = fileReader.read();
                while (true){
                    if (nextCh == -1){
                        throw new CSVFormatException("unexpected EOF");
                    }
                    if (nextCh == '"'){
                        nextCh = fileReader.read();
                        if (nextCh == ',')
                            break;
                        if (nextCh == '"')
                            strBuilder.append('"');
                        else
                            throw new CSVFormatException(String.format("invalid escape character \"%c", nextCh));
                    }else{
                        strBuilder.append((char) nextCh);
                    }
                    nextCh = fileReader.read();
                }
            }else {
                while (nextCh != ',' && nextCh != '\n' && nextCh != -1) {
                    if (nextCh == '"')
                        throw new CSVFormatException("escape character(\") found without surrounding double quote");
                    strBuilder.append((char) nextCh);
                    nextCh = fileReader.read();
                }
            }
            consumer.consume(strBuilder.toString(), index++);
            if (nextCh == '\n' || nextCh == -1)
                // end of line
                break;
            nextCh = fileReader.read();
            strBuilder.setLength(0);
        }
        return true;
    }

    public void close(){
        try {
            fileReader.close();
        } catch (IOException ignored) {
        }
    }

}
