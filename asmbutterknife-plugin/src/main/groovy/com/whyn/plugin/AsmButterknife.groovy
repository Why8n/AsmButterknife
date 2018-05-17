package com.whyn.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.whyn.asm.ClassInjector
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AsmButterknife extends Transform implements Plugin<Project> {

    private static final String TRANSFORM_NAME = AsmButterknife.class.getSimpleName()

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    public String getName() {
        return TRANSFORM_NAME
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    public boolean isIncremental() {
        return true
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        long start = System.currentTimeMillis()
        handleInputs(inputs, outputProvider)
        Log.v("consume total time: %d ms", System.currentTimeMillis() - start)
    }

    private void handleInputs(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectory(directoryInput, outputProvider)
            }

            input.jarInputs.each { JarInput jarInput ->
                handlerJarFile(jarInput, outputProvider)
            }
        }
    }

    private void injectFile(File file) {
        def name = file.name
        if (name.endsWith(".class")
                && !name.startsWith("R\$")
                && "R.class" != name
                && "BuildConfig.class" != name) {
            Log.v("start to scan file: %s", name)
            if (new ClassInjector(file).scan().write2File()) {
                Log.v(String.format("%s: insert byte code successfully", file.getName()))
            }
        }
    }

    private void handleDirectory(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            println "==== directoryInput.file = " + directoryInput.file
            directoryInput.file.eachFileRecurse { File file ->
                injectFile(file)
            }
        }
        // 获取output目录
        def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        // 将input的目录复制到output指定目录
        FileUtils.copyDirectory(directoryInput.file, dest)

    }

    private void handlerJarFile(JarInput jarInput, TransformOutputProvider outputProvider) {
//        println "------=== jarInput.file === " + jarInput.file.getAbsolutePath()
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {

        }
        /**
         * 重名输出文件,因为可能同名,会覆盖
         */
        def jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }
        //处理jar进行字节码注入处理
        def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        FileUtils.copyFile(jarInput.file, dest)
    }
}
