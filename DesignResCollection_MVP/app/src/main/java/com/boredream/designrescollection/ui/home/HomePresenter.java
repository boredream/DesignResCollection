package com.boredream.designrescollection.ui.home;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.net.HttpRequest;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View rootView;
    public List<DesignRes> datas;

    public HomePresenter(HomeContract.View rootView) {
        this.rootView = rootView;
        this.rootView.setPresenter(this);
    }

    @Override
    public void pullToLoadList() {
        loadData(1);
    }

    @Override
    public void loadList(final int page) {
        if(page == 1) {
            rootView.showProgress();
        }

        loadData(page);
    }

    private void loadData(final int page) {
        Observable<ListResponse<DesignRes>> observable = HttpRequest.getDesignRes(page);
        ObservableDecorator.decorate(observable).subscribe(
                new Subscriber<ListResponse<DesignRes>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!rootView.isActive()) {
                            return;
                        }
                        rootView.dismissProgress();

                        String error = ErrorInfoUtils.parseHttpErrorInfo(e);
                        rootView.showWebError(error);
                    }

                    @Override
                    public void onNext(ListResponse<DesignRes> response) {
                        if (!rootView.isActive()) {
                            return;
                        }
                        rootView.dismissProgress();

                        datas = response.getResults();
                        rootView.loadListSuccess(page, response.getResults());
                    }
                });
    }

}
