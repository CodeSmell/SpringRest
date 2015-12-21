package codesmell.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;

public class ReflectionUtil {
	public static void doInjectWithNoContainer(Object bean, Class<?> injectClass, Object injectObj){ 
		try {
			Field[] fields = bean.getClass().getDeclaredFields();
	
			for (Field f : fields) {
				Annotation inject = f.getAnnotation(Autowired.class);
				Class<?> fieldClass = f.getType();
				if ((inject != null) && (injectClass == fieldClass)){				
					f.setAccessible(true);
					f.set(bean, injectObj);
					f.setAccessible(false);
				}
			}
		}
		catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public static void doSet(Object bean, String name, Class<?> injectClass, Object injectObj){ 
		try {
			Field f = bean.getClass().getDeclaredField(name);
	
			if (f !=null){
				Class<?> fieldClass = f.getType();
				if (injectClass == fieldClass){				
					f.setAccessible(true);
					f.set(bean, injectObj);
					f.setAccessible(false);
				}				
			}
		}
		catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
}
