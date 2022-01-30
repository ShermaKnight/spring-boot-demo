package org.example.event;

import com.google.common.eventbus.Subscribe;

public class OptionEventListener {

    @Subscribe
    public void listener(OptionEvent optionEvent) {
        System.out.println(optionEvent.getData());
    }
}
