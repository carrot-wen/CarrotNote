package com.carrot.carrotnote.command;

import com.carrot.carrotnote.compile.PairTree;
import com.carrot.carrotnote.publish.Publisher;
import com.carrot.carrotnote.util.LogUtil;

import java.util.List;

public class DeleteCommand extends BaseCommand{

    private static final String TAG = "DeleteCommand";

    @Override
    public String[] cmd() {
        return new String[]{"delete"};
    }

    @Override
    public void exec(List<String> attrs, Publisher publisher) {
        try {
            int id = getBillId(attrs);
            source.delete(id);
        } catch (AnalysisException e) {
            LogUtil.E(TAG, e);
            publisher.showError(e.getMessage());
        }
    }


    private int getBillId(List<String> attrs) throws AnalysisException {
        PairTree tree = PairTree.build(attrs);
        String val = tree.get(ATTR_ID);

        if (val == null){
            throw new AnalysisException("Can't find attr : " + ATTR_ID);
        }

        try {
            return Integer.valueOf(val);
        } catch(RuntimeException e) {
            throw new AnalysisException(e.getMessage());
        }
    }
}
