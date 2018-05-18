package com.whyn.asm;

import com.whyn.asm.adapters.collect.CollectionClassAdapter;
import com.whyn.asm.adapters.inject.ViewInjectClassAdapter;
import com.whyn.asm.recorders.interfaces.impl.ClassReaderDispatcher;
import com.whyn.asm.recorders.interfaces.impl.ViewInjectCollector;
import com.whyn.asm.recorders.interfaces.impl.element.ViewInjectAnalyse;
import com.whyn.utils.Log;
import com.whyn.utils.Utils;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassInjector {
    private File mClassFile;
    private byte[] mClassByteCode;

    public ClassInjector(File classFile) {
        this.mClassFile = classFile;
        ViewInjectAnalyse.recordClassFile(classFile);
        ClassReaderDispatcher dispatcher = new ClassReaderDispatcher();
        ViewInjectCollector.getInstance().init(dispatcher);
        new ViewInjectRecorders(dispatcher).register();
    }

    public ClassInjector scan() throws IOException {
        Log.v("scanClassFile: %s", this.mClassFile.getAbsolutePath());
        this.mClassByteCode = Utils.file2bytes(new FileInputStream(this.mClassFile));
        ClassReader classReader = new ClassReader(this.mClassByteCode);
        classReader.accept(new CollectionClassAdapter(), ClassReader.SKIP_DEBUG + ClassReader.SKIP_FRAMES + ClassReader.SKIP_CODE);
        return this;
    }

    public boolean write2File() {
        return ViewInjectAnalyse.getViewInjectType() != ViewInject.NONE
                && Utils.write2file(this.mClassFile, inject());
    }

    private byte[] inject() {
        ClassReader classReader = new ClassReader(this.mClassByteCode);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ViewInjectClassAdapter(classWriter);
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        //verify the generated bytecode
        new ClassReader(classWriter.toByteArray()).accept(new CheckClassAdapter(new ClassWriter(0)), ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
