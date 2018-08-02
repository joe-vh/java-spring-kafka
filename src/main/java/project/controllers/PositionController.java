package project.controllers;

import project.security.UserDetailsFactory;
import project.services.PositionService;
import project.exceptions.InvalidConditionTypeException;
import project.models.Position;
import project.payload.request.PositionRequest;
import project.services.KafkaServiceImpl;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@CrossOrigin
@RestController
public class PositionController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    PositionService positionService;

    @Autowired
    KafkaServiceImpl kafkaService;

    @GetMapping("/api/positions")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Position> getPositions() {
        return positionService.getPositions();
    }

    @PostMapping("/api/positions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void createPosition(@Valid @RequestBody PositionRequest positionRequest) throws JSONException, InvalidConditionTypeException, ParseException {
        Long userId = UserDetailsFactory.getId();
        List<String> conditionTypes = Arrays.asList("future", "option", "cfd");

        Boolean invalidType = !conditionTypes.contains(positionRequest.getType());

        if (invalidType) {
            throw new InvalidConditionTypeException();
        }

        DateFormat df = new SimpleDateFormat(positionRequest.getMaturity().endsWith("Z") ? "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" : "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        positionService.createPosition(
            userId,
            positionRequest.getTickId(),
            positionRequest.getInstrumentId(),
            positionRequest.getBasketId(),
            positionRequest.getQuantity(),
            positionRequest.getPrice(),
            positionRequest.getSide(),
            positionRequest.getType(),
            positionRequest.getKind(),
            df.parse(positionRequest.getMaturity())
        );

        template.convertAndSend("/topic/reload/" + userId, "Position opened");
    }

    @GetMapping("/api/positions/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody Position getPosition(@PathVariable(value = "id") Long id) {
        return positionService.getPosition(id);
    }

    @GetMapping(value = "/api/positions", params = "basketId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Position> getPositionsByBasket(@RequestParam(value = "basketId") Long id) {
        return positionService.getAllPositionsByBasket(id);
    }

    @DeleteMapping("/api/positions/{id}")
    public void deletePosition(
        @PathVariable(value = "id") Long id
    ) throws JSONException {
        positionService.deletePosition(id);
    }
}
