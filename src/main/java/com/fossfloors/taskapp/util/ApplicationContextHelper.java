package com.fossfloors.taskapp.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Helper class to access Spring application context.
 */
@Component
public class ApplicationContextHelper {

  private static ApplicationContextHelper instance;

  @Autowired
  private ApplicationContext              applicationContext;

  @PostConstruct
  private void registerInstance() {
    instance = this;
  }

  /**
   * Returns an instance of the requested bean object.
   * 
   * @param <T>   - the bean type.
   * @param clazz - the bean class.
   * @return the requested object.
   */
  public static <T> T getBean(Class<T> clazz) {
    return instance.applicationContext.getBean(clazz);
  }

}