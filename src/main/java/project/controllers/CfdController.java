package project.controllers;

import project.services.CfdService;
import project.models.Cfd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class CfdController {

    @Autowired
    CfdService cfdService;

    @GetMapping("/api/cfds")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Cfd> getCfds() {
        return cfdService.getAllCfds();
    }

    @GetMapping("/api/cfds/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody Cfd getCfd(@PathVariable(value = "id") Long id) {
        return cfdService.getCfd(id);
    }
}
