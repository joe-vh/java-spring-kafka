package project.controllers;

import project.services.FutureService;
import project.models.Future;
import project.models.FutureTick;
import project.services.FutureTickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class FutureTickController {

    @Autowired
    FutureTickService futureTickService;

    @Autowired
    FutureService futureService;

    @GetMapping("/api/future-ticks")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<FutureTick> getLatestFutureTicks() {
        List<FutureTick> futureTicks = new ArrayList<>();
        List<Future> futures = futureService.getAllFutures();

        for (Future future : futures) {
            futureTicks.add(futureTickService.getLatestFutureTick(future.getId()));
        }

        return futureTicks;
    }

    @GetMapping(value = "/api/future-ticks", params = "futureId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<FutureTick> getFutureTicksByFuture(
            @RequestParam(value = "futureId") Long futureId
    ) {
        return futureTickService.getFutureTicks(futureId);
    }

    @GetMapping("/api/future-ticks/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public FutureTick getFutureTick(@PathVariable(value = "id") Long id) {
        return futureTickService.getFutureTick(id);
    }
}
