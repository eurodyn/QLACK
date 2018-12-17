package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionAttributesDTO {

    private Collection<SessionAttributeDTO> attributes;

}
