package org.example.repository;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class UserRepository {

    private static int primary = 1;
    public static ConcurrentHashMap<Integer, User> cache = new ConcurrentHashMap<>();

    private static List<String> addresses = new ArrayList<String>() {
        {
            add("中国,北京市,海淀区");
            add("中国,北京市,昌平区");
            add("中国,北京市,朝阳区");
            add("中国,北京市,西城区");
            add("中国,北京市,东城区");
        }
    };

    static {
        IntStream.range(1, 10).forEach(i -> cache.put(i, buildUser(i)));
    }

    private static User buildUser(Integer id) {
        User user = new User();
        user.setId(id);
        user.setUserName(RandomUtil.randomString(5) + " " + RandomUtil.randomString(7));
        user.setNickName(RandomUtil.randomString(16));
        user.setBirthday(RandomUtil.randomDay(-100, -10));
        user.setSalary(RandomUtil.randomDouble(10000d, 100000d));
        user.setAddresses(IntStream.range(1, RandomUtil.randomInt(10)).mapToObj(i -> buildAddress(user.getId(), user.getNickName())).collect(Collectors.toList()));
        return user;
    }

    private static Address buildAddress(Integer userId, String nickName) {
        Address address = new Address();
        address.setId(primary++);
        address.setUserId(userId);
        String[] random = addresses.get(RandomUtil.randomInt(addresses.size())).split(",");
        address.setCountry(random[0]);
        address.setCity(random[1]);
        address.setDetail(random[2]);
        address.setName(nickName);
        address.setCollect(RandomUtil.randomBoolean());
        return address;
    }

    public void reload() {
        cache.clear();
        IntStream.range(1, 10).forEach(i -> cache.put(i, buildUser(i)));
    }

}
