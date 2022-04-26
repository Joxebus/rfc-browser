package io.github.joxebus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RfcController {

    private static final String RFC_DIRPATH = "/Users/omarbautista/Documents/rfc-sources";

    @RequestMapping("/rfc/test")
    public String test() {
        return "HelloWorld";
    }

    @RequestMapping("/rfc/all")
    public List<String> rfcFileNames() {
        return getRFCs();
    }

    @RequestMapping("/rfc/{name}")
    public List<RfcResponse> rfcContentByName(@PathVariable String name) {
        List<String> filenameList = getRFCs();

        if (filenameList.contains(name.concat(".txt")))
            return null;

        List<RfcResponse> responses = new ArrayList<>();

        RfcResponse rfcResponse = new RfcResponse();
        rfcResponse.setFilename(name);

        List<RfcTextLine> textLines = new ArrayList<>();

        List<String> fileTextLines = getFileTextLines(new File(RFC_DIRPATH, name));


        for (int i = 0; i < fileTextLines.size(); i++) {
            RfcTextLine rfcTextLine = new RfcTextLine();
            rfcTextLine.setLine(i + 1);
            rfcTextLine.setText(fileTextLines.get(i));
            textLines.add(rfcTextLine);
        }

        rfcResponse.setContent(textLines);

        responses.add(rfcResponse);

        return responses;
    }

    @RequestMapping("/rfc/find")
    public List<RfcResponse> findText(@RequestParam("withText") String text) {
        text = text.toLowerCase();

        List<RfcResponse> responses = new ArrayList<>();

        for (String filename: getRFCs()) {

            RfcResponse rfcResponse = new RfcResponse();
            rfcResponse.setFilename(filename);

            List<RfcTextLine> textLines = new ArrayList<>();
            List<String> fileTextLines = getFileTextLines(new File(RFC_DIRPATH, filename));
            for (int i = 0; i < fileTextLines.size(); i++) {
                String line = fileTextLines.get(i).toLowerCase();
                if (line.contains(text)) {
                    RfcTextLine rfcTextLine = new RfcTextLine();
                    rfcTextLine.setLine(i + 1);
                    rfcTextLine.setText(fileTextLines.get(i));
                    textLines.add(rfcTextLine);
                    rfcResponse.setContent(textLines);
                }
            }

            if (!textLines.isEmpty())
                responses.add(rfcResponse);
        }

        return responses;
    }


    private String fileToString(File file) {
        List<String> fileTextLines = getFileTextLines(file);
        return String.join("\n", fileTextLines);
    }


    private List<String> getFileTextLines(File file) {
        List<String> fileTextLines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                fileTextLines.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileTextLines;
    }


    private List<String> getRFCs() {
        FilenameFilter onlyTxtFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
        File folder = new File(RFC_DIRPATH);
        return Arrays.asList(folder.list(onlyTxtFilter));
    }

}
