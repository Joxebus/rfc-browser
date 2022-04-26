package io.github.joxebus.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.joxebus.bean.RfcResponse;
import io.github.joxebus.service.RfcService;

@RestController
public class RfcController {

    private RfcService rfcService;

    public RfcController(RfcService rfcService) {
        this.rfcService = rfcService;
    }

    @RequestMapping("/rfc/all")
    public List<String> rfcFileNames() {
        return rfcService.getRfcFilenames();
    }

    @RequestMapping("/rfc/{filename}")
    public List<RfcResponse> rfcContentByName(@PathVariable String filename) {
        return List.of(rfcService.getRfcTextLinesAsResponse(filename));
    }

    @RequestMapping("/rfc/find")
    public List<RfcResponse> findText(@RequestParam("withText") String text) {
        return rfcService.findFilesWithText(text.toLowerCase());
    }

}
