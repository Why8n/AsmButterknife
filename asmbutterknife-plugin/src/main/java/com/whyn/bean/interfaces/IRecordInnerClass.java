package com.whyn.bean.interfaces;

import com.whyn.bean.element.InnerClassBean;

import java.util.List;

public interface IRecordInnerClass {
    boolean addInnerClass(InnerClassBean innerClassInfo);

    List<InnerClassBean> getInnerClass();
}
