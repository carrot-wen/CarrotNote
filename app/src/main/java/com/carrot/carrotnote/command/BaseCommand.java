package com.carrot.carrotnote.command;

import com.carrot.carrotnote.BillRepository;
import com.carrot.carrotnote.publish.Publisher;
import com.carrot.subcollection.Collect;

import java.util.List;

@Collect
public abstract class BaseCommand {

    public final static String ATTR_TIME = "-t";
    public final static String ATTR_NUMBER = "-c";
    public final static String ATTR_REASON = "-r";
    public final static String ATTR_ID = "-i";
    public final static String ATTR_NOTE = "-n";

    protected BillRepository source;

    public abstract String[] cmd();

    public String help() {
        return "unknown";
    }

    public abstract void exec(List<String> attrs, Publisher publisher);

    final public void setSource(BillRepository billRepository) {
        source = billRepository;
    }


    public interface Callback {
        void call(boolean success, String msg);
    }


    public static class AnalysisException extends Exception {

        public AnalysisException() {
            super();
        }

        public AnalysisException(String msg) {
            super(msg);
        }
    }
}
