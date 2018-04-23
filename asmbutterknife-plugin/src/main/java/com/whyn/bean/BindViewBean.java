package com.whyn.bean;

import com.whyn.bean.interfaces.IViewInject;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;

public class BindViewBean implements IViewInject<MethodVisitor>{
    private int id;
    private String name;
    private String desc;


    public BindViewBean() {
    }

    public BindViewBean(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result *= 31 + this.id;
        result *= 31 + this.name.hashCode();
        result *= 31 + this.desc.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        boolean bRet = false;
        do {
            if (bRet = (this == o))
                break;
            if (bRet = !(o instanceof BindViewBean))
                break;
            BindViewBean other = (BindViewBean) o;
            bRet = (this.id == other.id)
                    && (this.name == null ? other.name == null : this.name.equals(other.name))
                    && (this.desc == null ? other.desc == null : this.desc.equals(other.desc));
        } while (false);
        return bRet;
    }

    @Override
    public String toString() {
        return String.format("@BindView(%d)\n%s %s",this.id,this.desc,this.name);
    }

    @Override
    public void inject(MethodVisitor mv) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitLdcInsn(new Integer(2131165300));
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
        mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
        mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
    }
}
