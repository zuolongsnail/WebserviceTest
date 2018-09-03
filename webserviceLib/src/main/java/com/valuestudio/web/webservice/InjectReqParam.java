package com.valuestudio.web.webservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，封装webservice请求参数
 * 
 * @使用方法 index值为参数顺序，name为参数名称，如果不设置则默认变量名为参数名称
 * @WSReqParam(index = 1) private String uid;
 * @WSReqParam(name = "userId") private String uid;
 *
 * @description @Retention: 定义注解的保留策略
 * @Retention(RetentionPolicy.SOURCE) //注解仅存在于源码中，在class字节码文件中不包括
 * @Retention(RetentionPolicy.CLASS) //默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得
 * @Retention(RetentionPolicy.RUNTIME) //注解会在class字节码文件中存在，在运行时可以通过反射获取到
 * @Inherited //说明子类可以继承父类中的该注解
 *
 * @Target(ElementType.TYPE) //用于描述类、接口(包括注解类型) 或enum声明
 * @Target(ElementType.FIELD) //用于描述字段、枚举
 * @Target(ElementType.METHOD) //用于描述方法
 * @Target(ElementType.PARAMETER) //用于描述参数
 * @Target(ElementType.CONSTRUCTOR) //用于描述构造器
 * @Target(ElementType.LOCAL_VARIABLE) //用于描述局部变量
 * @Target(ElementType.ANNOTATION_TYPE)//注解
 * @Target(ElementType.PACKAGE) //用于描述包
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectReqParam {
	/**
	 * 请求参数的顺序，适用于webservice请求
	 * 
	 * @description 使用1,2,3,4,5,6,7....定义参数顺序，如果不定义，不会作为参数传递
	 * @return
	 * @author zuolong
	 */
	public int index() default 0;

	/**
	 * 请求参数的名称，适用于webservice和http请求
	 * 
	 * @description 须与接口文档中的参数名称定义相同，不设置则默认变量名为参数的名称
	 * @return
	 * @author zuolong
	 */
	public String name() default "";
}