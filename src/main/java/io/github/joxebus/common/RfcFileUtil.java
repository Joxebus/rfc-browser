package io.github.joxebus.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;

public final class RfcFileUtil {

    public static RfcResponse textLinesToResponse(final List<RfcTextLine> textLines, final String filename) {
        RfcResponse rfcResponse = new RfcResponse();
        rfcResponse.setFilename(filename);
        rfcResponse.setContent(textLines);
        return rfcResponse;
    }

    public static List<String> getFileNames(final String directory) {
        FilenameFilter onlyTxtFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
        File folder = new File(directory);
        return Arrays.asList(folder.list(onlyTxtFilter));
    }
}
