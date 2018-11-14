package com.eurodyn.qlack.fuse.security.manager;

import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class AAAUser extends User {

    private boolean superadmin;

    private boolean external;
    private Set<UserAttributeDTO> userAttributes;

    /**
     * The session Id created for this user.
     * Expect this to be populated, only, when attempting to login the user.
     */
    private String sessionId;

    /**
     * Indicates whether this user was found to be active or not. What is
     * regarded as an 'active status' is specified on the .cfg file of this
     * bundle.
     */
    private boolean active;

    /**
     * The status of the user is set only when the user is found to be inactive,
     * so that end-applications can differentiate the reason why a user could
     * not be authenticated.
     */
    private Integer status;

    /**
     * Call to parent class constructor.
     */
    public AAAUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    /**
     * Call to parent class constructor.
     */
    public AAAUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
        boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
