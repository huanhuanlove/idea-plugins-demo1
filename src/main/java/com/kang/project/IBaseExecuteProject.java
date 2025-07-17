package com.kang.project;

import com.intellij.openapi.project.Project;

/**
 * 项目相关操作 {@link com.intellij.openapi.project.Project}
 *
 * @author Kang
 */
public interface IBaseExecuteProject {

    /**
     * 前置处理器
     */
    default void preExecute(Project project) {

    };

    /**
     * 业务操作
     */
    default void businessExecute(Project project) {

    };

    /**
     * 后置处理器
     */
    default void postExecute(Project project) {

    };
}
