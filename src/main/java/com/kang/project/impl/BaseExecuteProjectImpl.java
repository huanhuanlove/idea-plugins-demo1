package com.kang.project.impl;

import com.intellij.openapi.project.Project;
import com.kang.project.IBaseExecuteProject;

/**
 * 项目相关操作 {@link com.intellij.openapi.project.Project}
 *
 * 重写接口通用方法操作项目
 *
 * @author Kang
 */
public class BaseExecuteProjectImpl implements IBaseExecuteProject {

    @Override
    public void preExecute(Project project) {
        IBaseExecuteProject.super.preExecute(project);
    }

    @Override
    public void businessExecute(Project project) {
        IBaseExecuteProject.super.businessExecute(project);
    }

    @Override
    public void postExecute(Project project) {
        IBaseExecuteProject.super.postExecute(project);
    }
}
