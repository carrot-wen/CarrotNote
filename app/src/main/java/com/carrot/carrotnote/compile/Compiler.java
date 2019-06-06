package com.carrot.carrotnote.compile;

import com.carrot.carrotnote.command.BaseCommand;
import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.util.StringConcat;

import java.util.ArrayList;
import java.util.List;

public class Compiler {
    private StringConcat mStringConcat;

    private static Compiler compiler;

    private Compiler() {
        mStringConcat = new StringConcat();
    }

    public static Compiler get() {
        if (compiler == null) {
            synchronized (Compiler.class) {
                if (compiler == null) {
                    compiler = new Compiler();
                }
            }
        }
        return compiler;
    }

    public static String build(String cmd,Bill bill) {
        StringConcat concat = new StringConcat();
        concat.add(cmd);
        if (bill != null) {
            concat.add(BaseCommand.ATTR_TIME).add(String.valueOf(bill.getTime()))
                    .add(BaseCommand.ATTR_NUMBER).add(String.valueOf(bill.getNumber()))
                    .add(BaseCommand.ATTR_REASON).add(bill.getReason())
                    .add(BaseCommand.ATTR_ID).add(String.valueOf(bill.getId()));
        }


        return concat.concat();
    }

    public static List<Pair> compile(String cmd) throws BaseCommand.AnalysisException {
        List<String> phrase = get().mStringConcat.split(cmd);
        return compile(phrase);
    }

    public static List<Pair> compile(List<String> phrase) throws BaseCommand.AnalysisException {
        if (phrase.size() % 2 != 0) {
            // TODO: 2019/5/17 nou sure
            throw new BaseCommand.AnalysisException("why ??????");
        }

        List<Pair> pairs = new ArrayList<>(phrase.size() / 2);

        for (int i = 0; i < phrase.size(); i = i + 2) {
            Pair pair = new Pair(phrase.get(i), phrase.get(i + 1));
            pairs.add(pair);
        }
        return pairs;
    }



}
