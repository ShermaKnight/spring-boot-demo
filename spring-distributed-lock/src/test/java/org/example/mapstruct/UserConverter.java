package org.example.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mappings({
            @Mapping(target = "userName", source = "name"),
            @Mapping(target = "status", expression = "java(java.lang.String.valueOf(user.getStatus()))"),
            @Mapping(target = "birthday", dateFormat = "yyyy-MM-dd")
    })
    UserDto toUserDto(User user);
}
