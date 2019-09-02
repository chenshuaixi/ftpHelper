# ftpHelper
抢先向ftp服务器上传指定命名的文件     

使用方式：  
修改源程序中的ftp的ip,port,username,password等参数，再将源文件及其依赖库打包为jar文件，然后运行如下：  
java -jar param1 param2 param3  
参数解释:   
param1 --- 本地待上传文件的绝对地址，例如: C:\Users\jeave\Desktop\test.xls  
param2 --- 指定FTP服务器上接收文件的一个独一无二的文件夹名字，例如: 20190907-20190911  
param2 --- 指定上传的文件在FTP服务器上的名字，例如: testname.xls 
