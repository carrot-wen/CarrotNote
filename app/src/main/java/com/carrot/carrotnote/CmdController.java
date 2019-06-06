package com.carrot.carrotnote;

import com.carrot.carrotnote.command.BaseCommand;
import com.carrot.carrotnote.publish.Publisher;
import com.carrot.carrotnote.util.LogUtil;
import com.carrot.carrotnote.util.StringConcat;
import com.carrot.subcollection.SubCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdController {
    private static final String TAG = "CmdController";
    private Map<String, BaseCommand> mCommandMap;
    private final static String UNFIND_ERROR = "cannot find this cmd :   ";
    private final static String EMPTY_ERROR = "empty cmd :   ";
    private StringConcat mConcat;

    public void init(BillRepository billRepository) {
        mCommandMap = new HashMap<>();
        List<Class<? extends BaseCommand>> commands = SubCollector.get(BaseCommand.class);
        mConcat = new StringConcat();
        for (Class<? extends BaseCommand> aClass : commands) {
            BaseCommand command = null;
            try {
                command = aClass.newInstance();
                command.setSource(billRepository);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            if (command == null) {
                continue;
            }

            String[] cmds = command.cmd();
            for (String cmd : cmds) {
                mCommandMap.put(cmd, command);
            }
        }

        for (String s : mCommandMap.keySet()) {
            LogUtil.d(TAG, "init: cmd has " + s);
        }
    }

    public void exec(String cmd,Publisher publisher) {
        LogUtil.d(TAG, "exec:  " + cmd);
        List<String> list = mConcat.split(cmd);
        if (list.isEmpty()) {
            publisher.showError(EMPTY_ERROR + cmd);
            return;
        }
        BaseCommand command = mCommandMap.get(list.get(0));
        if (command == null) {
            publisher.showError(UNFIND_ERROR + cmd);
            return;
        }

        LogUtil.d(TAG, "exec: send to " + command.getClass().getSimpleName());
        ArrayList<String> content = new ArrayList<>(list.size());
        content.addAll(list);
        content.remove(0);
        command.exec(content, publisher);
    }
}
