package project.controllers;

import project.services.CfdService;
import project.services.CfdTickService;
import project.models.Cfd;
import project.models.CfdTick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class CfdTickController {

    @Autowired
    CfdTickService cfdTickService;

    @Autowired
    CfdService cfdService;

    @GetMapping("/api/cfd-ticks")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<CfdTick> getLatestCfdTicks() {
        List<CfdTick> cfdTicks = new ArrayList<>();
        List<Cfd> cfds = cfdService.getAllCfds();

        for (Cfd cfd : cfds) {
            cfdTicks.add(cfdTickService.getLatestCfdTick(cfd.getId()));
        }

        return cfdTicks;
    }

    @GetMapping(value = "/api/cfd-ticks", params = "cfdId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<CfdTick> getCfdTicksByCfd(
            @RequestParam(value = "cfdId") Long cfdId
    ) {
        return cfdTickService.getCfdTicks(cfdId);
    }

    @GetMapping("/api/cfd-ticks/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody CfdTick getCfdTick(@PathVariable(value = "id") Long id) {
        return cfdTickService.getCfdTick(id);
    }
}
