package project.controllers;

import project.services.BasketService;
import project.services.PositionService;
import project.models.Basket;
import project.models.Position;
import project.payload.request.BasketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
public class BasketController {

    @Autowired
    BasketService basketService;

    @Autowired
    PositionService positionService;

    private static final Logger LOG = LoggerFactory.getLogger(BasketController.class);

    @GetMapping("/api/baskets")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody List<Basket> getBaskets() {
        return basketService.getBaskets();
    }

    @PostMapping("/api/baskets")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void createBasket(@Valid @RequestBody BasketRequest basketRequest) {
        basketService.addBasket(basketRequest.getName());
    }

    @GetMapping("/api/baskets/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody Basket getBasket(@PathVariable(value = "id") Long id) {
        Basket basket = basketService.getBasket(id);
        List<Position> positions = positionService.getAllPositionsByBasket(id);
//        LOG.info("size", positions.size());
        basket.setPositions(positions);

        return basket;
    }
}
