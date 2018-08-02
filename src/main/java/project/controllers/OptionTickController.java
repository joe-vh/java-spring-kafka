package project.controllers;

import project.services.OptionTickService;
import project.models.Option;
import project.models.OptionTick;
import project.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class OptionTickController {

    @Autowired
    OptionTickService optionTickService;

    @Autowired
    OptionService optionService;

    @GetMapping("/api/option-ticks")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<OptionTick> getLatestOptionTicks() {
        List<OptionTick> optionTicks = new ArrayList<>();
        List<Option> options = optionService.getAllOptions();

        for (Option option : options) {
            optionTicks.add(optionTickService.getLatestOptionTick(option.getId()));
        }

        return optionTicks;
    }

    @GetMapping(value = "/api/option-ticks", params = "optionId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<OptionTick> getOptionTicks(
            @RequestParam(value = "optionId") Long optionId
    ) {
        return optionTickService.getOptionTicks(optionId);
    }

    @GetMapping("/api/option-ticks/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody OptionTick getOptionTick(@PathVariable(value = "id") Long id) {
        return optionTickService.getOptionTick(id);
    }
}
