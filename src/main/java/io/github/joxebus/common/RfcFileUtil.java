package io.github.joxebus.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;
import io.github.joxebus.exception.RfcException;

public final class RfcFileUtil {

    public static final String TXT_EXTENSION = ".txt";

    public static RfcResponse textLinesToResponse(final List<RfcTextLine> textLines, final String filename) {
        RfcResponse rfcResponse = new RfcResponse();
        rfcResponse.setFilename(filename);
        rfcResponse.setContent(textLines);
        return rfcResponse;
    }

    public static List<String> getFileNames(final String directory) {
        try {
            FilenameFilter onlyTxtFilter = (dir, name) -> name.endsWith(TXT_EXTENSION);
            File folder = new File(directory);
            return List.of(Objects.requireNonNull(folder.list(onlyTxtFilter)));
        } catch (Exception e) {
            throw new RfcException(String.format("There was a problem with the directory [%s]", directory));
        }

    }

    public static List<String> readFileTextLines(final String directory, final String filename) {
        List<String> fileTextLines = new ArrayList<>();
        try {
            File file = new File(directory, filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (Objects.nonNull(line)) {
                fileTextLines.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(fileTextLines);
    }
}
