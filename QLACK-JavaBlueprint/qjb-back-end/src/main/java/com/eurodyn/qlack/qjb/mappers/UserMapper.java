package com.eurodyn.qlack.qjb.mappers;

import com.eurodyn.qlack.qjb.dto.UserDTO;
import com.eurodyn.qlack.qjb.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
  User toEntity(UserDTO userDTO);
  UserDTO toDTO(User user);
}
