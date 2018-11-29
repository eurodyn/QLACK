package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * Implements Spring's GrantedAuthority interface.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class GroupDTO extends BaseDTO implements GrantedAuthority {

    private String name;

    private String objectId;

    private String description;

    private String parentId;

    private Set<GroupDTO> children;

    public GroupDTO(String id) {
        setId(id);
    }

    /**
     * Returns the group's name as role/authority.
     *
     * @return Group name as role name
     */
    @Override
    public String getAuthority() {
        return name;
    }

}
