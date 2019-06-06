package com.carrot.carrotnote.command;

import com.carrot.carrotnote.compile.Pair;
import com.carrot.carrotnote.compile.PairTree;
import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.publish.Publisher;
import com.carrot.carrotnote.util.LogUtil;

import java.util.List;

public class AddCommand extends BaseCommand {

    private static final String TAG = "AddCommand";

    /*
       -t -name -count -r
     */

    @Override
    public String[] cmd() {
        return new String[]{"add", "ADD", "Add"};
    }

    @Override
    public void exec(List<String> attrs, Publisher publisher) {
        LogUtil.d(TAG, "exec: get cmd " + attrs);
        Bill bill = null;
        try {
            LogUtil.d(TAG, "exec: use getBill " + attrs +" .");
            bill = getBill(attrs);
            LogUtil.d(TAG, "exec: getbill " + bill.toString());
            source.add(bill);
            publisher.showInfo(bill.toString());
        } catch (AnalysisException e) {
            LogUtil.d(TAG, "exec: AnalysisException " + e.getCause());
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
        } catch(Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, "getBill: Exception " + e.getMessage());
            throw new AnalysisException(e.getMessage());
        }

        return bill;
    }




    @Override
    public String help() {
        return "add bill";
    }
}
