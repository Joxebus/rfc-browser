package io.github.joxebus.service;

import java.util.List;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.bean.RfcTextLine;

public interface RfcService {
    String EMPTY_STRING = "";

    List<RfcResponse> findFilesWithText(String text);

    RfcResponse getRfcTextLinesAsResponse(String filename);

    RfcResponse getRfcTextLinesAsResponse(String filename, String filter);

    List<RfcTextLine> getRfcTextLines(String filename, String filter);

    List<String> getRfcFilenames();
}
