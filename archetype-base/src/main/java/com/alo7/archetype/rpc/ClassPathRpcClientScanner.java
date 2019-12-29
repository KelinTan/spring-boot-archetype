// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rpc;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kelin Tan
 */
@Log4j2
public class ClassPathRpcClientScanner extends ClassPathScanningCandidateComponentProvider {
    private ClassLoader classLoader;

    public ClassPathRpcClientScanner(Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader) {
        super(false, environment);
        this.classLoader = classLoader;
        setResourceLoader(resourceLoader);
        addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        if (metadata.isInterface()) {
            try {
                Class<?> target = ClassUtils.forName(metadata.getClassName(), classLoader);
                return !target.isAnnotation();
            } catch (Exception e) {
                log.error("load class exception:", e);
            }
        }
        return false;
    }

    public Set<BeanDefinition> scan(List<String> basePackages) {
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        basePackages.forEach(basePackage -> {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            beanDefinitions.addAll(candidates);
        });
        return beanDefinitions;
    }
}
