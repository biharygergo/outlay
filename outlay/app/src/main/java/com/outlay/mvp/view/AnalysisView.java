package com.outlay.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Report;

import java.util.List;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public interface AnalysisView extends MvpView {
    void showAnalysis(Report report);

    void setCategories(List<Category> categories);
}
