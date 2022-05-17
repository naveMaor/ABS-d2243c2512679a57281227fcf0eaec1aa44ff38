package data.File;

import customes.Client;
import data.Database;
import loan.Loan;
import time.Timeline;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperty;

public class DataToFileDataFromFile implements Serializable{

    private Map<String, List<Loan>> loanMapByCategory;
    private Map<String, Client> clientMap;
    private int currTime;

    public void SaveDataToFile() throws IOException {

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream out = null;
        try {
            Path currentRelativePath = Paths.get("");
            File f = new File(currentRelativePath.toAbsolutePath().toString(), "/ABS-data.bin");
            f.getParentFile().mkdirs();
            fileOutputStream = new FileOutputStream(f);
            out = new ObjectOutputStream(fileOutputStream);

            this.loanMapByCategory = Database.getLoanMapByCategory();
            this.clientMap = Database.getClientMap();
            this.currTime = Timeline.getCurrTime();
            out.writeObject(this);
            out.close();
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            out.close();
            fileOutputStream.close();
            //System.out.println("because you annoy me :)");
            throw e;
        }
    }

    public static void LoadDataFromFile(DataToFileDataFromFile dataToFileDataFromFile) throws IOException, ClassNotFoundException {

        Path currentRelativePath = Paths.get("");
        File f = new File(currentRelativePath.toAbsolutePath().toString(), "ABS-data.bin");
        FileInputStream fileInputStream = new FileInputStream(f.getAbsolutePath());

        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        Object obj = in.readObject();
        dataToFileDataFromFile = (DataToFileDataFromFile)obj;
        in.close();
        fileInputStream.close();
        Database.setClientMap(dataToFileDataFromFile.clientMap);
        Database.setLoanMapByCategory(dataToFileDataFromFile.loanMapByCategory);
        Timeline.setCurrTime(dataToFileDataFromFile.currTime);
    }

}
