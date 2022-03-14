package org.example.mapstruct;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapStruct测试用例")
public class MapStructTest {

    @Test
    @DisplayName("对象转换")
    public void converter() {
        User user = User.builder().name(RandomUtil.randomStringUpper(10)).status(1).birthday(RandomUtil.randomDay(-100, -1)).build();
        System.out.println(user);
        UserDto userDto = UserConverter.INSTANCE.toUserDto(user);
        System.out.println(userDto);
    }
}
