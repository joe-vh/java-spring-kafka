package project.security;

import project.models.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetailsFactory {
    // eagerly instantiate singleton
    private static final Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    private UserDetailsFactory() {}
    public static Object getInstance() {
        return userDetails;
    }

    public static Long getId() {
        if (userDetails instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) userDetails).getId();
        }
        // mocked userId for test
        return 1L;
    }
}


