package project.controllers;

import project.services.PositionService;
import project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PositionService positionService;

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Object> getUsers(@RequestParam(value = "username") String username) {
        // commenting ElasticSearch for test build
        // String url = "http://0.0.0.0:9200/search/_search?q=" + username;
        // RestTemplate restTemplate = new RestTemplate();
        // return restTemplate.getForObject(url, String.class);
        return userService.getAllUsersAndStrategiesByUsername(username);
    }
}
