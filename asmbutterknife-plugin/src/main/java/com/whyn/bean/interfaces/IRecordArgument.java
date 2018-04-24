package com.whyn.bean.interfaces;

import com.google.common.collect.ImmutableList;
import com.whyn.bean.element.ArgumentBean;

public interface IRecordArgument {

    boolean addArgument(ArgumentBean args);

    ImmutableList<ArgumentBean> getArgument();
}
