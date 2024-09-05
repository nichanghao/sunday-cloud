package net.sunday.cloud.system.converter;

import net.sunday.cloud.system.controller.admin.menu.vo.MenuUpsertReqVO;
import net.sunday.cloud.system.model.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuConverter {

    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    MenuDO vo2do(MenuUpsertReqVO vo);

}
