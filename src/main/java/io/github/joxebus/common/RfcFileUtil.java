package io.github.joxebus.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;
import io.github.joxebus.exception.RfcException;

public final class RfcFileUtil {

    private static final String TXT_EXTENSION = ".txt";

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
}
