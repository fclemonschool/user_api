package com.fclemonschool.user.mapper;

import com.fclemonschool.user.model.entity.AuthCode;
import com.fclemonschool.user.model.request.AuthCodeRequest;
import com.fclemonschool.user.model.response.AuthCodeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct 라이브러리를 통해 Dto, Entity, Vo 사이의 변환을 처리한다.
 */
@Mapper(componentModel = "spring")
public interface AuthCodeMapper extends EntityMapper<AuthCodeRequest, AuthCodeResponse, AuthCode> {
  AuthCodeMapper INSTANCE = Mappers.getMapper(AuthCodeMapper.class);
}
