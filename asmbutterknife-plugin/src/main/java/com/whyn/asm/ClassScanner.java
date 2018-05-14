package com.whyn.asm;

import com.whyn.asm.adapters.collect.CollectionClassAdapter;
import com.whyn.asm.adapters.inject.ViewInjectClassAdapter;
import com.whyn.utils.Log;
import com.whyn.utils.Utils;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClassScanner {
    private File mClassFile;
    private byte[] mClassByteCode;

    public ClassScanner(File classFile) {
        this.mClassFile = classFile;
        ViewInjectClassRecorder.getInstance().recordClassFile(classFile);
    }

    public ClassScanner scan() throws IOException {
        Log.v("scanClassFile: %s", this.mClassFile.getAbsolutePath());
        this.mClassByteCode = Utils.file2bytes(new FileInputStream(this.mClassFile));
        ClassReader classReader = new ClassReader(this.mClassByteCode);
        classReader.accept(new CollectionClassAdapter(), ClassReader.SKIP_DEBUG + ClassReader.SKIP_FRAMES + ClassReader.SKIP_CODE);
        return this;
    }

    public boolean write2File() {
        if (ViewInjectAnalyse.getViewInjectType() == ViewInject.NONE)
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

    private byte[] inject() {
        ClassReader classReader = new ClassReader(this.mClassByteCode);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//        ClassVisitor cv = new TraceClassVisitor(classWriter, new PrintWriter(System.out));
//        ClassVisitor cv = new CheckClassAdapter(classWriter);
//        cv = new ViewInjectClassAdapter(cv);
        ClassVisitor cv = new ViewInjectClassAdapter(classWriter);
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
