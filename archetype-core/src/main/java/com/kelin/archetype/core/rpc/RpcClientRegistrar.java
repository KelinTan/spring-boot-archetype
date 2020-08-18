// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(
                RpcClientScan.class.getName()));
        if (annotationAttributes == null) {
            return;
        }
        List<String> basePackages = geScanPackages(annotationAttributes);
        ClassPathRpcClientScanner classScanner = new ClassPathRpcClientScanner(environment, classLoader,
                resourceLoader);
        Set<BeanDefinition> beanDefinitionSet = classScanner.scan(basePackages);

        beanDefinitionSet.forEach(beanDefinition -> {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                registerBean(((AnnotatedBeanDefinition) beanDefinition));
            }
        });
    }

    private List<String> geScanPackages(AnnotationAttributes annotationAttributes) {
        List<String> basePackages = new ArrayList<>();
        for (String pkg : annotationAttributes.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annotationAttributes.getStringArray("basePackages")) {
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

            RpcClient annotation = target.getAnnotation(RpcClient.class);
            String endpoint = this.environment.resolvePlaceholders(annotation.endpoint());
            return Proxy.newProxyInstance(RpcClient.class.getClassLoader(), new Class[] {target},
                    new RpcClientProxy(target, endpoint, annotation.errorHandler().newInstance()));
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("error: ", e);
        }
        return null;
    }
}
