package com.whyn.asm.adapters.bindview;

import com.whyn.bean.BindViewBean;
import com.whyn.bean.ViewInjectBean;
import com.whyn.define.Const;
import com.whyn.utils.AsmClassLoader;
import com.whyn.utils.Log;
import com.whyn.utils.Utils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.Set;

public class BindViewMethodAdapter extends MethodVisitor {
    private ViewInjectBean mViewInjectBean;

    public BindViewMethodAdapter(MethodVisitor mv, ViewInjectBean viewInjectBean) {
        super(Const.ASM_API, mv);
        this.mViewInjectBean = viewInjectBean;
    }

    //enter this method means that the method fits "onCreate((Landroid/os/Bundle;)V)"
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
//        class = com.yn.asmbutterknife.MainActivity
//        opcode=183,owner=android/support/v7/app/AppCompatActivity,name=onCreate,desc=(Landroid/os/Bundle;)V,itf=false
        Log.v("%s:visitMethodInsn: opcode=%d,owner=%s,name=%s,desc=%s,itf=%b",
                BindViewMethodAdapter.class.getSimpleName(),
                opcode, owner, name, desc, itf);
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        Log.v("load class: %s",this.mViewInjectBean.getInternalClassName());
        Class<?> srcTypeCls = null;
        //            srcTypeCls = ClassLoader.getSystemClassLoader().loadClass(this.mViewInjectBean.getClassName());
//            srcTypeCls = Type.SHORT_TYPE.getClass().getClassLoader().loadClass(this.mViewInjectBean.getClassName());
        //        Class<?> srcTypeCls = new AsmClassLoader().defineClass(this.mViewInjectBean.getClassName(),
//                this.mViewInjectBean.getByteCode());
        if ("setContentView".equals(name)
                && "(I)V".equals(desc)
                && Utils.isActivity(srcTypeCls)) {
            injectActivity();
        }
    }

    private void injectActivity() {
        Set<BindViewBean> beans = this.mViewInjectBean.getBindViewBeans();
        for (BindViewBean bean : beans) {
//            bean.inject(this.mv);
        }
    }
}
