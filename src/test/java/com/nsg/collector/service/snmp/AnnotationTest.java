package com.nsg.collector.service.snmp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;

import com.nsg.collector.service.snmp.annotation.MibObjectType;
import com.nsg.collector.service.snmp.mibobject.SystemInfo;

public class AnnotationTest {
	@Test
	public void testAnnotation() {
		// AnnotationHelper helper=new AnnotationHelper();
		// helper.get(SystemInfo.class);
		Class aclass = SystemInfo.class;
		Field[] fields = aclass.getDeclaredFields();
		for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			MibObjectType mibobject=  field.getAnnotation(MibObjectType.class);
			//System.out.println(mibobject.oid());
			//System.out.println(mibobject.type());
//			for (Annotation annotation : annotations) {
//				System.out.println(annotation.toString());
//			}

		}

	}
	
	@Test
	public <T> void testSetvalue() throws InstantiationException, IllegalAccessException{
		Class aclass = SystemInfo.class;
	    T t=(T) aclass.newInstance();
	    Field[] fields = aclass.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(t, "ss");
		}
		
		System.out.println(t.toString());
	}
	
}
