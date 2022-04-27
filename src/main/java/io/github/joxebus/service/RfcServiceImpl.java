package io.github.joxebus.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;
import io.github.joxebus.common.RfcFileUtil;
import io.github.joxebus.exception.RfcException;

@Service
public class RfcServiceImpl implements RfcService {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final String RFC_DIR_PATH = System.getenv("RFC_DIR_PATH");

    private List<String> rfcFilenames;

    @Override
    public List<RfcResponse> findFilesWithText(final String text) {
        log.info("Looking for text: {}", text);
        List<RfcResponse> responses = new ArrayList<>();
        getRfcFilenames().stream().parallel().forEach( filename -> {
            try {
                RfcResponse rfcResponse = getRfcTextLinesAsResponse(filename, text);
                if (!rfcResponse.getContent().isEmpty()) {
                    responses.add(rfcResponse);
                }
            } catch (RfcException rfcException) {
                log.error("The file [{}] cannot be processed", filename);
            }
        });
        return Collections.unmodifiableList(responses);
    }

    @Override
    public RfcResponse getRfcTextLinesAsResponse(final String filename) {
        return getRfcTextLinesAsResponse(filename, EMPTY_STRING);
    }

    @Override
    public RfcResponse getRfcTextLinesAsResponse(final String filename, final String filter) {
        List<RfcTextLine> rfcTextLines = getRfcTextLines(filename, filter);
        return RfcFileUtil.textLinesToResponse(rfcTextLines, filename);
    }

    @Override
    public List<RfcTextLine> getRfcTextLines(final String filename, final String filter) {
        log.debug("Reading file [{}] with filter [{}]", filename, filter);
        if(Objects.isNull(filename) || !getRfcFilenames().contains(filename)) {
            log.warn("The file [{}] is not valid ", filename);
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


    @Override
    public List<String> getRfcFilenames() {
        if(Objects.isNull(rfcFilenames) || rfcFilenames.isEmpty()) {
            log.info("Loading RFC filenames from path [{}]", RFC_DIR_PATH);
            rfcFilenames = RfcFileUtil.getFileNames(RFC_DIR_PATH);
        }
        return Collections.unmodifiableList(rfcFilenames);
    }


}
