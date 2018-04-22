package com.eurodyn.qlack.fuse.settings.mappers;

import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SettingMapper {

  List<SettingDTO> map(List<Setting> o);

  SettingDTO map(Setting o);

  @Mappings({
      @Mapping(source = "group", target = "name")
  })
  GroupDTO mapToGroupDTO(Setting o);

  List<GroupDTO> mapToGroupDTO(List<Setting> o);

}
