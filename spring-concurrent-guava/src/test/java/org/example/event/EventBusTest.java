package org.example.event;

import cn.hutool.core.util.RandomUtil;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EventBus测试用例")
public class EventBusTest {

    @Test
    @DisplayName("发送事件")
    public void publisher() {
        EventBus bus = new EventBus();
        bus.register(new OptionEventListener());

        OptionEvent optionEvent = new OptionEvent();
        optionEvent.setData("发送事件: " + RandomUtil.randomStringUpper(10));
        bus.post(optionEvent);
    }
}
