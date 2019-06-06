package com.carrot.carrotnote.util;

import java.util.Arrays;
import java.util.List;

public class StringConcat extends ConcatAdapter<String> {


    @Override
    public String concat(List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        builder.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            builder.append(ConcatAdapter.SEPARATOR).append(list.get(i));
        }
        return builder.toString();
    }

    @Override
    public List<String> split(String msg) {
        return Arrays.asList(msg.split(ConcatAdapter.SEPARATOR));
    }
}
