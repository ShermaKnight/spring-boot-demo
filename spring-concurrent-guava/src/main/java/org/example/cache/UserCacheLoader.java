package org.example.cache;

import cn.hutool.core.util.RandomUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import org.example.domain.entity.Address;
import org.example.domain.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserCacheLoader extends CacheLoader<Integer, User> {

    private static final AtomicInteger ADDRESS_ID = new AtomicInteger(1);
    private static final List<String> ADDRESS_CITY = new ArrayList<String>() {
        {
            add("北京");
            add("上海");
            add("广州");
            add("深圳");
            add("成都");
            add("重庆");
            add("杭州");
            add("苏州");
        }
    };

    @Override
    public ListenableFuture<User> reload(Integer key, User oldValue) throws Exception {
        return super.reload(key, oldValue);
    }

    @Override
    public User load(Integer key) throws Exception {
        User user = new User();
        user.setId(key);
        user.setUserName(RandomUtil.randomStringUpper(10));
        user.setBirthday(RandomUtil.randomDay(-1000, -10));
        user.setSalary(RandomUtil.randomDouble(10000.0d, 1000000.0d));
        user.setCreateTime(new Date());

        Address address = new Address();
        address.setId(ADDRESS_ID.getAndAdd(1));
        address.setCity(RandomUtil.randomEle(ADDRESS_CITY));
        user.setAddresses(Stream.of(address).collect(Collectors.toList()));
        return user;
    }
}
