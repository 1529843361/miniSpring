package com.minis;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : hanyang
 * @date : 2023-12-30 01:58
 **/

@Slf4j
public class ClassPathXmlApplicationContext {

    private List<BeanDefinition> beanDefinitions = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();

    // 构造器获取外部配置，解析出bean的定义，形成内存映像

    public ClassPathXmlApplicationContext(String fileName) {
        this.readXml(fileName);
        this.instanceBeans();
    }

    private void readXml(String fileName) {

        SAXReader saxReader = new SAXReader();
        try {
            URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            // 对配置文章中的每一个<bean>,进行处理
            for (Element element : rootElement.elements()) {
                // 获取bean的基本信息
                String beanID = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
                // 将bean的定义放到beanDefinitions
                beanDefinitions.add(beanDefinition);
            }
        } catch (Exception e) {
            log.error("readXml exception:", e);
        }
    }

    private void instanceBeans() {
        try {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                singletons.put(beanDefinition.getId(),
                        Class.forName(beanDefinition.getClassName()).newInstance());
            }
        } catch (Exception e) {
            log.error("instanceBeans exception:", e);
        }

    }

    // 这时对外的一个方法，让外部程序从容器中获取bean实例，会逐步演化成核心方法
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
