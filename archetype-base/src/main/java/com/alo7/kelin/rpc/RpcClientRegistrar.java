// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.rpc;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * @author Kelin Tan
 */
@Log4j2
@SuppressWarnings("SpellCheckingInspection")
public class RpcClientRegistrar
        implements
        ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware, BeanFactoryAware {
    private static final String BASE_PACKAGE = "com.alo7.kelin";

    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;
    private ClassLoader classLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        registerRpcClient();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private void registerRpcClient() {
        ClassPathScanningCandidateComponentProvider classScanner = getRpcClientScanner();
        Set<BeanDefinition> beanDefinitionSet = classScanner.findCandidateComponents(BASE_PACKAGE);

        beanDefinitionSet.forEach(beanDefinition -> {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                registerBean(((AnnotatedBeanDefinition) beanDefinition));
            }
        });
    }

    private ClassPathScanningCandidateComponentProvider getRpcClientScanner() {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false, this.environment) {
                    @Override
                    protected boolean isCandidateComponent(
                            AnnotatedBeanDefinition beanDefinition) {
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
                };

        provider.setResourceLoader(this.resourceLoader);
        provider.addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
        return provider;
    }

    private void registerBean(AnnotatedBeanDefinition definition) {
        String className = definition.getBeanClassName();
        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(className, createProxy(definition));
    }

    private Object createProxy(AnnotatedBeanDefinition definition) {
        try {
            AnnotationMetadata annotationMetadata = definition.getMetadata();
            Class<?> target = Class.forName(annotationMetadata.getClassName());
            return Proxy.newProxyInstance(RpcClient.class.getClassLoader(), new Class[] {target}, new RpcClientProxy());
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }
}
