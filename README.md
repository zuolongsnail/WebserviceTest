一个利用运行时注解和反射实现参数封装和数据解析的Webservice请求库，见webserviceLib文件夹，app文件夹是demo。

在工作中使用webservice请求的时候需要单独写参数的设置以及请求后的数据解析，于是对这块进行了封装，利用了运行时注解和反射机制实现参数封装，数据返回使用json格式数据，利用反射实现解析封装。
不足：
1.利用运行时注解和反射在使用时效率不高；
2.参数的封装和解析可以使用GSON实现；

详细介绍和使用请访问：https://blog.csdn.net/zuolongsnail/article/details/82259260
