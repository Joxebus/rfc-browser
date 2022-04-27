package io.github.joxebus.service;

import static io.github.joxebus.common.RfcFileUtil.TXT_EXTENSION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;
import io.github.joxebus.common.RfcFileUtil;
import io.github.joxebus.exception.RfcException;

@Service
public class RfcService {

    private static final String RFC_DIR_PATH = System.getenv("RFC_DIR_PATH");
    public static final String EMPTY_STRING = "";

    private List<String> rfcFilenames;

    public List<RfcResponse> findFilesWithText(final String text) {
        List<RfcResponse> responses = new ArrayList<>();
        for (String filename: getRfcFilenames()) {
            try {
                RfcResponse rfcResponse = getRfcTextLinesAsResponse(filename, text);
                if (!rfcResponse.getContent().isEmpty()) {
                    responses.add(rfcResponse);
                }
            } catch (RfcException rfcException) {
                rfcException.printStackTrace();
            }
        }
        return responses;
    }

    public RfcResponse getRfcTextLinesAsResponse(final String filename) {
        return getRfcTextLinesAsResponse(filename, EMPTY_STRING);
    }

    public RfcResponse getRfcTextLinesAsResponse(final String filename, final String filter) {
        List<RfcTextLine> rfcTextLines = getRfcTextLines(filename, filter);
        return RfcFileUtil.textLinesToResponse(rfcTextLines, filename);
    }

    public List<RfcTextLine> getRfcTextLines(final String filename, final String filter) {

        if(Objects.isNull(filename) || getRfcFilenames().contains(filename.concat(TXT_EXTENSION))) {
            return Collections.emptyList();
        }

        List<String> fileTextLines = RfcFileUtil.readFileTextLines(RFC_DIR_PATH, filename);

        // Since Java 1.8 we can use Streams to iterate.
        return IntStream.range(0, fileTextLines.size())
                .filter( index -> fileTextLines.get(index).contains(filter))
                .mapToObj( index -> {
                    RfcTextLine rfcTextLine = new RfcTextLine();
                    rfcTextLine.setLine(index + 1);
                    rfcTextLine.setText(fileTextLines.get(index));
                    return rfcTextLine;
                }).toList();
    }


    public List<String> getRfcFilenames() {
        if(Objects.isNull(rfcFilenames) || rfcFilenames.isEmpty()) {
            rfcFilenames = RfcFileUtil.getFileNames(RFC_DIR_PATH);
        }
        return rfcFilenames;
    }


}
