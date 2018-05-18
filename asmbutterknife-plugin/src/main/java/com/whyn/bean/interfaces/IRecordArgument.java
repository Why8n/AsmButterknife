package com.whyn.bean.interfaces;

import com.whyn.bean.element.ArgumentBean;

import java.util.List;

public interface IRecordArgument {

    boolean addArgument(ArgumentBean args);

    List<ArgumentBean> getArgument();
}
