// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import com.kelin.archetype.common.log.LogMessageBuilder;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClassPathRpcClientScanner extends ClassPathScanningCandidateComponentProvider {
    private final ClassLoader classLoader;

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
                log.error(LogMessageBuilder.builder()
                        .message("Load class exception")
                        .parameter("bean", beanDefinition)
                        .build(), e);
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
