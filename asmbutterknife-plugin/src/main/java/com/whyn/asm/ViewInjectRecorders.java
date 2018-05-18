package com.whyn.asm;

import com.whyn.asm.recorders.interfaces.IClassReader;
import com.whyn.asm.recorders.interfaces.impl.ClassReaderDispatcher;
import com.whyn.asm.recorders.interfaces.impl.element.AccessMethodRecorder;
import com.whyn.asm.recorders.interfaces.impl.element.BindViewRecorder;
import com.whyn.asm.recorders.interfaces.impl.element.ClassRecorder;
import com.whyn.asm.recorders.interfaces.impl.element.InnerClassRecorder;
import com.whyn.asm.recorders.interfaces.impl.element.OnClickRecorder;
import com.whyn.asm.recorders.interfaces.impl.element.ViewInjectTypeRecorder;

public class ViewInjectRecorders {
    protected ClassReaderDispatcher mDispatcher;
    private IClassReader[] mClassReaders = new IClassReader[]{
            new ClassRecorder(),
            new AccessMethodRecorder(),
            new InnerClassRecorder(),
            new ViewInjectTypeRecorder(),
            new BindViewRecorder(),//bindview must in front of onclick
            new OnClickRecorder(),
    };

    public ViewInjectRecorders(ClassReaderDispatcher dispatcher) {
        this.mDispatcher = dispatcher;
    }

    public void register() {
        for (IClassReader reader : this.mClassReaders) {
            this.mDispatcher.register(reader);
        }
    }

}
