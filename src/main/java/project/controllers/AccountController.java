package project.controllers;

import project.models.Account;
import project.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/api/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public @ResponseBody Account getAccount() {
         return accountService.getAccount();
    }

}
