package webcrawling;

import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class WebCrawler {

    // Method to find hyperlinks from a list of WebElements
    public static List<String> findHyperLinks(List<WebElement> lnks) {
        List<String> listOfURL = new ArrayList<String>();
        for (WebElement wElements : lnks) {
            // checking weather element is null or not
            if (wElements.equals(null))
                continue;
            else {

                listOfURL.add(wElements.getAttribute("href"));
            }
        }
        // remove link if it is null
        listOfURL.remove(null);
        return (listOfURL);
    }

    // Method to write content to a file
    public static void contentWrite(String nameOfFolder, String content, String fileName, String extension) {
        try {
            File checkFolder = new File(nameOfFolder);
            File f = new File(nameOfFolder + fileName + extension);
            if (!checkFolder.exists()) {
                // Try to create the directory
                boolean created = checkFolder.mkdirs(); // This will create the folder and any necessary but nonexistent parent directories.

                if (created) {
                    // Folder created successfully
                    FileWriter fWriter = new FileWriter(f, false);
                    fWriter.write(content);
                    fWriter.close();
                } else {
                    System.out.println("Failed to create the folder.");
                }
            } else {
                // Folder already exists, write content to the file
                FileWriter fWriter = new FileWriter(f, false);
                fWriter.write(content);
                fWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurring in file");
        }
    }

    // Method to create a file, write content, and store URL in a Hashtable
    public static Hashtable<String, String> createFile(String url, String cntnt, String nameOfFile,
                                                       String folder) {
        Hashtable<String, String> mapOfURL = new Hashtable<String, String>();
        mapOfURL.put(nameOfFile + ".html", url);
        contentWrite(folder, cntnt, nameOfFile, ".html");
        return mapOfURL;
    }
}
