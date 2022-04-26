package io.github.joxebus.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;
import io.github.joxebus.common.RfcFileUtil;

@Service
public class RfcService {

    private static final String RFC_DIR_PATH = System.getenv("RFC_DIR_PATH");

    private List<String> rfcFilenames;

    public List<RfcResponse> findFilesWithText(final String text) {

        List<RfcResponse> responses = new ArrayList<>();
        for (String filename: getRfcFilenames()) {

            RfcResponse rfcResponse = new RfcResponse();
            rfcResponse.setFilename(filename);

            List<RfcTextLine> textLines = new ArrayList<>();
            List<String> fileTextLines = getFileTextLines(new File(RFC_DIR_PATH, filename));
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

    public RfcResponse getRfcTextLinesAsResponse(String filename) {
        List<RfcTextLine> rfcTextLines = getRfcTextLines(filename);
        return RfcFileUtil.textLinesToResponse(rfcTextLines, filename);
    }

    public List<RfcTextLine> getRfcTextLines(String filename) {
        List<String> filenameList = getRfcFilenames();

        if (filenameList.contains(filename.concat(".txt")))
            return null;

        List<RfcTextLine> rfcTextLines = new ArrayList<>();
        List<String> fileTextLines = getFileTextLines(new File(RFC_DIR_PATH, filename));


        for (int i = 0; i < fileTextLines.size(); i++) {
            RfcTextLine rfcTextLine = new RfcTextLine();
            rfcTextLine.setLine(i + 1);
            rfcTextLine.setText(fileTextLines.get(i));
            rfcTextLines.add(rfcTextLine);
        }
        return rfcTextLines;
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


    public List<String> getRfcFilenames() {
        if(Objects.isNull(rfcFilenames) || rfcFilenames.isEmpty()) {
            rfcFilenames = RfcFileUtil.getFileNames(RFC_DIR_PATH);
        }
        return rfcFilenames;
    }


}
