package com.whyn.asm;

import com.whyn.asm.adapters.collect.CollectionClassAdapter;
import com.whyn.asm.adapters.inject.ViewInjectClassAdapter;
import com.whyn.bean.ViewInjectBean;
import com.whyn.utils.Log;
import com.whyn.utils.Utils;
import com.yn.annotations.ViewInject;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ClassScanner {
    private File mClassFile;
    private ViewInjectBean mViewInjectBean;

    public ClassScanner(File classFile) {
        this.mClassFile = classFile;
    }

    public ClassScanner scan() throws IOException {
        byte[] bytes = Utils.file2bytes(new FileInputStream(this.mClassFile));
        this.mViewInjectBean = new ViewInjectBean(bytes);
        ClassReader classReader = new ClassReader(bytes);
//        this.mClassWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//        ViewInjectClassAdapter viewInjectClassAdapter = new ViewInjectClassAdapter(this.mClassWriter, this.mViewInjectBean);
//        classReader.accept(viewInjectClassAdapter, ClassReader.EXPAND_FRAMES);
        classReader.accept(new CollectionClassAdapter(this.mViewInjectBean), ClassReader.SKIP_DEBUG);
        return this;
    }

    private byte[] inject() {
        ClassReader classReader = new ClassReader(this.mViewInjectBean.getByteCode());
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new TraceClassVisitor(classWriter, new PrintWriter(System.out));
        cv = new ViewInjectClassAdapter(cv, this.mViewInjectBean);
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    public boolean write2File() {
        Log.v("enter write2File:whichView=%d", this.mViewInjectBean.getWhichView());
        if (this.mViewInjectBean.getWhichView() == ViewInject.NONE)
            return false;
        boolean bRet = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.mClassFile);
            fos.write(inject());
            bRet = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.close(fos);
        }
        return bRet;
    }
}
