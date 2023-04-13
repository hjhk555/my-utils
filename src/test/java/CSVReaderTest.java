import indi.hjhk.csv.CSVReader;
import indi.hjhk.log.Logger;

import java.io.IOException;

public class CSVReaderTest {
    public static void main(String[] args) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader("./csv/1101.csv");
            csvReader.readHeader();

            for (int i = 0; i < 1000; i++) {
                csvReader.next();
                Logger.logOnStdout("timestamp=%d, carId=%s, vehicletype=%d, vlpc=%d",
                        csvReader.getLong("TIME"), csvReader.getString("VLP"), csvReader.getLong("VEHICLETYPE"), csvReader.getLong("VLPC"));
            }

            csvReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CSVReader.CSVFormatException e) {
            csvReader.close();
            throw new RuntimeException(e);
        }
    }
}
