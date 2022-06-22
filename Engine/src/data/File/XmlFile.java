package data.File;

import data.SchemaBasedJAXB;
import data.schema.generated.AbsDescriptor;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Scanner;

public class XmlFile implements Serializable {
    private static InputStream inputStream = null;
    private static AbsDescriptor inputObject;

    public static AbsDescriptor getInputObject() {
        return inputObject;
    }


    public static void getDetailsForFile(){
        Scanner scanner = new Scanner(System.in);
        boolean inputValidation = false;
        String filePath;
        System.out.println("Please insert input file path and note it must be an XML file:");
        while (!inputValidation){
            try{
                filePath = scanner.nextLine();
                if(filePath.charAt(filePath.length()-1) == '"' || filePath.charAt(0) == '"'){
                    System.out.println("ERROR: Please provide file path without quotation marks");
                }else{
                    fileNameCheck(filePath);
                    inputObject = createInputObjectFromPath(filePath);
                    inputValidation = true;
                }
            }catch (Exception exception){
                System.out.println("ERROR: Something went wrong with this action:" + exception.getMessage());
                System.out.println("please type path again:");
            }
        }
    }
    private final static void fileNameCheck(String fileName) throws Exception {
        String fileFormat = ".xml";
        int stringLen = fileName.length();
        if (fileName.substring(stringLen - 4, stringLen).compareTo(fileFormat) != 0)
            throw new Exception("Incorrect file format, Please insert an XML file only");
    }
    public static AbsDescriptor createInputObjectFromPath(String pathName) throws Exception {
        try {
            inputStream = new FileInputStream(new File(pathName));
            AbsDescriptor descriptor = SchemaBasedJAXB.deserializeFrom(inputStream);
            //isCopySucceeded = true;
            return descriptor;

            //System.out.println("name of first country is: " + descriptor.getAbsCustomers().getAbsCustomer().get(0).getName());
        } catch ( FileNotFoundException e) {
            throw new Exception("Invalid path name, file doesn't exist.");
        } catch (JAXBException j){

            throw new Exception("Couldn't read file");
        }
    }

    public static void resetFileData(){
        inputStream = null;
        inputObject = null;
    }

    public static void createInputObjectFromFile(File selectedFile) throws FileNotFoundException, JAXBException {
            inputStream = new FileInputStream(selectedFile);
            AbsDescriptor descriptor = SchemaBasedJAXB.deserializeFrom(inputStream);
            inputObject= descriptor;
    }
}
