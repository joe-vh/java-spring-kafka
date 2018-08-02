package project.controllers;

import project.exceptions.InvalidConditionTypeException;
import project.models.Strategy;
import project.payload.request.StrategyRequest;
import project.services.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class StrategyController {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyController.class);

    @Autowired
    StrategyService strategyService;

    @GetMapping("/api/strategies")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Strategy> getStrategies() {
        return strategyService.getStrategies();
    }

    @PostMapping("/api/strategies")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void createStrategy(@Valid @RequestBody StrategyRequest strategyRequest) {
        List<Strategy> strategies;
        List<String> conditionTypes = Arrays.asList("<", "==", ">");
//        ETick eTick = null;
        Integer invalidConditions = strategyRequest.getConditions().stream().filter(c -> !conditionTypes.contains(c.getCondition())).collect(Collectors.toList()).size();

//        LOG.info("invalidConditions" + invalidConditions.toString());

        if (invalidConditions != 0) {
            throw new InvalidConditionTypeException();
        }

        LOG.info(strategyRequest.getSide().toString());
        LOG.info(strategyRequest.getConditions().get(0).toString());

        strategyService.addStrategy(
            strategyRequest.getName(),
            strategyRequest.getBasketId(),
            strategyRequest.getQuantity(),
            strategyRequest.getSide(),
            strategyRequest.getOpen(),
            strategyRequest.getFutureId(),
            strategyRequest.getOptionId(),
            strategyRequest.getCfdId(),
            strategyRequest.getConditions()
        );
    }

    @GetMapping(value = "/api/strategies", params = "basketId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Strategy> getStrategiesByBasket(@RequestParam(value = "basketId") Long id) {
        return strategyService.getStrategiesByBasketId(id);
    }

    @DeleteMapping("/api/strategies/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteStrategy(
            @PathVariable(value = "id") Long id
    ) {
        strategyService.deleteStrategy(id);
    }
}
