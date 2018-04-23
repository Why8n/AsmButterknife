package com.whyn.asm.adapters.inject;

import android.support.annotation.NonNull;

import com.whyn.asm.adapters.base.BaseMethodVisitor;
import com.whyn.bean.BindViewBean;
import com.whyn.bean.ViewInjectBean;
import com.whyn.utils.Log;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.Set;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;

public class InjectActivityMethodAdapter extends BaseMethodVisitor {

    private ViewInjectBean mViewInjectBean;

    public InjectActivityMethodAdapter(MethodVisitor mv, @NonNull ViewInjectBean viewInjectBean) {
        super(mv);
        this.mViewInjectBean = viewInjectBean;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        if ("setContentView".equals(name) && "(I)V".equals(desc)) {
            Log.v("enter setContentView,begin to inject findViewById");
            Set<BindViewBean> beans = this.mViewInjectBean.getBindViewBeans();
            for (BindViewBean bean : beans) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitLdcInsn(new Integer(bean.getId()));
//                mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, this.mViewInjectBean.getInternalClassName(), "findViewById", "(I)Landroid/view/View;", false);
//                mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
                mv.visitTypeInsn(CHECKCAST, Type.getType(bean.getDesc()).getInternalName());
//                mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
                mv.visitFieldInsn(PUTFIELD, this.mViewInjectBean.getInternalClassName(), bean.getName(), bean.getDesc());

            }

        }
    }
}
