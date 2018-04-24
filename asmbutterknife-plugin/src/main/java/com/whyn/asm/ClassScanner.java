package com.whyn.asm;

import com.whyn.asm.adapters.collect.CollectionClassAdapter;
import com.whyn.asm.adapters.inject.ViewInjectClassAdapter;
import com.whyn.bean.Tuple;
import com.whyn.bean.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Utils;
import com.yn.asmbutterknife.annotations.ViewInject;

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
    //    private ViewInjectBean mViewInjectBean;
    private byte[] mClassByteCode;

    public ClassScanner(File classFile) {
        this.mClassFile = classFile;
    }

    public ClassScanner scan() throws IOException {
        this.mClassByteCode = Utils.file2bytes(new FileInputStream(this.mClassFile));
        ClassReader classReader = new ClassReader(this.mClassByteCode);
        classReader.accept(new CollectionClassAdapter(), ClassReader.SKIP_DEBUG);
        return this;
    }

    private byte[] inject() {
        ClassReader classReader = new ClassReader(this.mClassByteCode);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new TraceClassVisitor(classWriter, new PrintWriter(System.out));
        cv = new ViewInjectClassAdapter(cv);
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    public boolean write2File() {
        Tuple<MethodBean, AnnotationBean> viewInjectDetail = ViewInjectClassRecorder.getInstance().getViewInjectDetail();
        if (viewInjectDetail == null
                || Utils.<Integer>getProperValue(viewInjectDetail.second.getValue(), ViewInject.NONE).intValue() == ViewInject.NONE)
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
