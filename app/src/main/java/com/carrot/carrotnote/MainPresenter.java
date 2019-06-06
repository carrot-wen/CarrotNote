package com.carrot.carrotnote;


import com.carrot.carrotnote.publish.Publisher;

public class MainPresenter implements IPresenter {

    private BillRepository mRepository;
    private CmdController mCmdController;
    private Publisher mPublisher;

    public MainPresenter(Publisher publisher) {
        mRepository = MyApplication.getRepository();
        mCmdController = new CmdController();
        mPublisher = publisher;
    }



    @Override
    public void exec(String cmd) {
        mCmdController.exec(cmd,mPublisher);
    }

    @Override
    public void load() {
        mCmdController.init(mRepository);
    }

}
