package com.whyn.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project;


public class AsmButterknife implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        registerTransform(android)
    }

    private void registerTransform(BaseExtension android){
        android.registerTransform(new BindViewTransform())
    }
}
