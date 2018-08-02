package project.controllers;

import project.payload.request.SignalRequest;
import project.services.PositionService;
import project.services.SignalService;
import project.exceptions.InvalidUrlException;
import project.models.Signal;
import project.models.Position;
import org.apache.commons.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class SignalController {
    private static final Logger LOG = LoggerFactory.getLogger(SignalController.class);
    private static final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    @Autowired
    SignalService signalService;

    @Autowired
    PositionService positionService;

    @GetMapping("/api/signals")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Signal> getSignals() {
        return signalService.getSignals();
    }

    @PostMapping("/api/signals")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void createSignal(@Valid @RequestBody SignalRequest signalRequest) {
        if (urlValidator.isValid(signalRequest.getUrl())) {
            signalService.addSignal(signalRequest.getName(), signalRequest.getUrl());
        } else {
            throw new InvalidUrlException();
        }
    }

    @GetMapping(value = "/api/signals", params = "instrumentId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Object> getSignal(@RequestParam(value = "instrumentId") Long instrumentId) {
        List<Signal> signals;
        List<Position> positions;
        List<Object> results = new ArrayList<>();
        String exception = null;

        signals = signalService.getSignals();
        LOG.info("signals");

        for(Signal s : signals) {
            RestTemplate restTemplate = new RestTemplate();

            String url = s.getUrl() + "?instrumentId=" + instrumentId;
            String answer = "Buy"; // restTemplate.getForObject(url, String.class);

            results.add(new Object() {
                public final String name = s.getName(); public final String signal = answer;
            });
        }

        return results;
    }
}


