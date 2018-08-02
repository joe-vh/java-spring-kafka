package project.controllers;

import project.services.FutureService;
import project.models.Future;
import project.services.FutureTickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class FutureController {

    @Autowired
    FutureService futureService;

    @Autowired
    FutureTickService futureTickService;

    @GetMapping("/api/futures")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Future> getFutures() {
        return futureService.getAllFutures();
    }

    @GetMapping("/api/futures/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody Future getFuture(@PathVariable(value = "id") Long id) {
        return futureService.getFuture(id);
    }
}
