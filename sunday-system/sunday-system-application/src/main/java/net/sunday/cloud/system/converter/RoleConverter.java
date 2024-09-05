package net.sunday.cloud.system.converter;

import net.sunday.cloud.system.controller.admin.role.vo.RoleRespVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleUpsertReqVO;
import net.sunday.cloud.system.model.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    RoleDO vo2do(RoleUpsertReqVO vo);

    RoleRespVO do2vo(RoleDO _do);

}
