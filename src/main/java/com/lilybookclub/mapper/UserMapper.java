package com.lilybookclub.mapper;
import com.lilybookclub.dto.response.user.SignUpResponse;
import com.lilybookclub.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    SignUpResponse toResponse(User user);
}