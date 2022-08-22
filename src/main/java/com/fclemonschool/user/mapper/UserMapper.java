package com.fclemonschool.user.mapper;

import com.fclemonschool.user.model.entity.User;
import com.fclemonschool.user.model.request.UserRequest;
import com.fclemonschool.user.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct 라이브러리를 통해 Dto, Entity, Vo 사이의 변환을 처리한다.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserRequest, UserResponse, User> {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
