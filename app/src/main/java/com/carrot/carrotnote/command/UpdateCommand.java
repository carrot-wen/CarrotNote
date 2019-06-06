package com.carrot.carrotnote.command;

import com.carrot.carrotnote.compile.Compiler;
import com.carrot.carrotnote.compile.PairTree;
import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.publish.Publisher;
import com.carrot.carrotnote.util.LogUtil;

import java.util.List;

public class UpdateCommand extends BaseCommand{
    private static final String TAG = "UpdateCommand";

    @Override
    public String[] cmd() {
        return new String[]{"update"};
    }

    @Override
    public void exec(List<String> attrs, Publisher publisher) {
        try {
            Bill bill = getBill(attrs);
            source.update(bill);
        } catch (AnalysisException e) {
            LogUtil.E(TAG, e);
            publisher.showError(e.getMessage());
        }
    }

    private Bill getBill(List<String> attrs) throws AnalysisException {
        PairTree tree = PairTree.build(attrs);
        tree.printTree();
        Bill bill;
        try {
            bill = new Bill(Long.valueOf(tree.get(ATTR_TIME)),
                    Double.valueOf(tree.get(ATTR_NUMBER)),tree.get(ATTR_REASON),tree.get(ATTR_NOTE));
            bill.setId(Integer.valueOf(tree.get(ATTR_ID)));
        } catch(Exception e) {
            LogUtil.E(TAG, e);
            throw new AnalysisException(e.getMessage());
        }

        return bill;
    }
}
