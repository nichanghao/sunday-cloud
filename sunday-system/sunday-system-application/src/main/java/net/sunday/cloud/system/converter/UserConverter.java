package net.sunday.cloud.system.converter;

import net.sunday.cloud.system.controller.admin.user.vo.UserRespVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserUpsertReqVO;
import net.sunday.cloud.system.model.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE) // 忽略未知属性编译告警
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDO vo2do(UserUpsertReqVO vo);

    UserRespVO do2vo(UserDO _do);

}
