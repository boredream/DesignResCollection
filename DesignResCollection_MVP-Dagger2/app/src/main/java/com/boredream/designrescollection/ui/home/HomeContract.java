package com.boredream.designrescollection.ui.home;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;
import com.boredream.designrescollection.entity.DesignRes;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void loadListSuccess(int page, List<DesignRes> datas);

    }

    interface Presenter extends BasePresenter {

        void loadList(int page);

        void pullToLoadList();

    }
}
