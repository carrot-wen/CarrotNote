package com.carrot.carrotnote.compile;

import com.carrot.carrotnote.command.BaseCommand;
import com.carrot.carrotnote.util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PairTree {
    private static final String TAG = "PairTree";
    private Map<String,String> mTree;


    private PairTree(Map<String, String> tree) {
        mTree = tree;
    }



    public static PairTree build(List<String> attrs) throws BaseCommand.AnalysisException {
        List<Pair> pairs = Compiler.compile(attrs);
        Map<String,String> tree = new HashMap<>();
        for (Pair pair : pairs) {
            String val = tree.get(pair.attr);
            if (val != null) {
                throw new BaseCommand.AnalysisException();
            }
            tree.put(pair.attr,pair.val);
        }
        return new PairTree(tree);
    }


    public String get(String key) {
        return mTree.get(key);
    }

    public void printTree(){
        for (String key : mTree.keySet()) {
            LogUtil.d(TAG, "printTree:   [" + key +"] --->  [" + mTree.get(key)+"].");
        }
    }
}
