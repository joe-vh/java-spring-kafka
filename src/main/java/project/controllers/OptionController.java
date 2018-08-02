package project.controllers;

import project.models.Option;
import project.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class OptionController {

    @Autowired
    OptionService optionService;

    @GetMapping("/api/options")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Option> getOptions() {
        return optionService.getAllOptions();
    }

    @GetMapping("/api/options/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody Option getOption(@PathVariable(value = "id") Long id) {
        return optionService.getOption(id);
    }
}
