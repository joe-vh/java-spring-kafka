package project.controllers;

import project.models.Condition;
import project.services.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ConditionController {

    @Autowired
    ConditionService conditionService;

    @GetMapping("/api/conditions")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Condition> getConditions() {
        return conditionService.getConditions();
    }
}
