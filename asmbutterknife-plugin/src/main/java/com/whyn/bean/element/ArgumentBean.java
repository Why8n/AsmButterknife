package com.whyn.bean.element;

import com.whyn.bean.Tuple;

import java.util.ArrayList;
import java.util.List;

public final class ArgumentBean {
    private List<Tuple<String, String>> args = new ArrayList<>();

    public boolean addArgument(String typeDesc, String name) {
        return this.args.add(new Tuple<>(typeDesc, name));
    }
}
