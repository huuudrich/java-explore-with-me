package ru.practicum.huuudrich.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class CopyNonNullProperties {
    public static void copy(Object src, Object target) {
        BeanWrapper srcWrapper = new BeanWrapperImpl(src);
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        PropertyDescriptor[] pds = srcWrapper.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null && srcWrapper.getPropertyValue(pd.getName()) != null) {
                targetWrapper.setPropertyValue(pd.getName(), srcWrapper.getPropertyValue(pd.getName()));
            }
        }
    }
}
