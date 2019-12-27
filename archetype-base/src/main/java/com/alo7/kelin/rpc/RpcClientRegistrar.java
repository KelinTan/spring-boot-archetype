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
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kelin Tan
 */
@Log4j2
@SuppressWarnings("SpellCheckingInspection")
public class RpcClientRegistrar
        implements
        ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware, BeanFactoryAware {
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;
    private ClassLoader classLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        registerRpcClient(meta);
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

    private void registerRpcClient(AnnotationMetadata metadata) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(
                RpcClientScan.class.getName()));
        if (annoAttrs == null) {
            return;
        }
        List<String> basePackages = geScanPackages(annoAttrs);
        ClassPathRpcClientScanner classScanner = new ClassPathRpcClientScanner(environment, classLoader,
                resourceLoader);
        Set<BeanDefinition> beanDefinitionSet = classScanner.scan(basePackages);

        beanDefinitionSet.forEach(beanDefinition -> {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                registerBean(((AnnotatedBeanDefinition) beanDefinition));
            }
        });
    }

    private List<String> geScanPackages(AnnotationAttributes annoAttrs) {
        List<String> basePackages = new ArrayList<>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        return basePackages;
    }

    private void registerBean(AnnotatedBeanDefinition definition) {
        String className = definition.getBeanClassName();
        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(className, createProxy(definition));
    }

    private Object createProxy(AnnotatedBeanDefinition definition) {
        try {
            AnnotationMetadata annotationMetadata = definition.getMetadata();
            Class<?> target = Class.forName(annotationMetadata.getClassName());

            String endpoint = this.environment.getProperty(
                    RpcUtils.getProperty(target.getAnnotation(RpcClient.class).host()));

            return Proxy.newProxyInstance(RpcClient.class.getClassLoader(), new Class[] {target},
                    new RpcClientProxy(target, endpoint));
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }
}
