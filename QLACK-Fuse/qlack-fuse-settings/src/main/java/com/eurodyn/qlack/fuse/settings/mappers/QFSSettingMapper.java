package com.eurodyn.qlack.fuse.settings.mappers;

import com.eurodyn.qlack.fuse.settings.dto.QFSGroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.QFSSettingDTO;
import com.eurodyn.qlack.fuse.settings.model.QFSSetting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QFSSettingMapper {

  List<QFSSettingDTO> map(List<QFSSetting> o);

  QFSSettingDTO map(QFSSetting o);

  @Mappings({
      @Mapping(source = "group", target = "name")
  })
  QFSGroupDTO mapToGroupDTO(QFSSetting o);

  List<QFSGroupDTO> mapToGroupDTO(List<QFSSetting> o);

}
