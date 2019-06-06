package com.carrot.carrotnote.command;

import com.carrot.carrotnote.publish.Publisher;

import java.util.List;


public class GetCommand extends BaseCommand {


    @Override
    public String[] cmd() {
        return new String[]{"get"};
    }

    @Override
    public void exec(List<String> attr, Publisher publisher) {
        getBills(publisher);
    }


    private void getBills(Publisher publisher) {
         source.getBills(publisher::show);
    }
}
