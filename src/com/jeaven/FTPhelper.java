package com.jeaven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPhelper {
	//定义ftp的参数
	public static String host;
	public static int port;
	public static String username;
	public static String password;
	public static int defaultTimeout;
	public static String LOCAL_CHARSET;
	public static String FTP_REQURED_CHARSET;
	
	//配置ftp的参数
	static {
		try {  
			host = "202.38.246.199";
			port = 2100;
			username = "wang_lin_ge";
			password = "123456";
			defaultTimeout = 3600000;  //设置超时时间为1小时
			LOCAL_CHARSET = "GBK";  //初始默认的编码是本地的GBK编码
			FTP_REQURED_CHARSET = "iso-8859-1";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * @param savedPath 存入ftp服务器的路径
	 * @param localfile 本地文件
	 * @param savedFileName 保存在ftp服务器的名字
	 * */
	/*
	public String uploadFile(String savedPath, File localFile, String savedFileName) {
		FileInputStream fis;  //输入文件流
		boolean flag = false;
		try {
			fis = new FileInputStream(localFile);	//建立文件流
			FTPClient ftpClient = new FTPClient();	//建立ftp客户端
			ftpClient.setControlEncoding(encode);	//设置编码
			ftpClient.connect(host, port);	//设置连接主机ip和port
			ftpClient.login(username, password);	//设置用户名和密码
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	//设置ftp传输文件类型
			ftpClient.enterLocalPassiveMode();	//采取被动传输模式
			ftpClient.setDataTimeout(defaultTimeout); 	//设置默认传输超时时间
			ftpClient.setBufferSize(1024*1024);	//设置缓冲区1M
			//针对性的做出优化
			ftpClient.changeWorkingDirectory("booking");
			//判断是否存在指定的文件路径
			//if(!JudgeHasFileDir(ftpClient, savedPath)) return "不存在指定的文件夹路径";
			while(!JudgeHasFileDir(ftpClient, savedPath));	//如果ftp服务器下不存在指定的文件夹，就一直循环
			//切换工作目录到保存的路径下面，如果不存在就创建一个目录
			boolean isFileDirExisted = ftpClient.changeWorkingDirectory(savedPath);
			if(isFileDirExisted == false) {   //如果不存在就创造一个路径，切换工作目录
				ftpClient.makeDirectory(savedPath);
				ftpClient.changeWorkingDirectory(savedPath);
			}
			flag = ftpClient.storeFile(savedFileName, fis);  //上传ftp
			fis.close();
			//关闭ftp连接
			ftpClient.logout();
			ftpClient.disconnect();
			
		} catch (SocketException e) {
			e.printStackTrace();
			return "SOCKET NOT SUCCESS";
		} catch(IOException e) {
			e.printStackTrace();
			return "IO NOT SUCCESS";
		}
		if(flag) {
			return "SUCCESS";
		} else {
			return "NOT SUCCESS";
		}
	}
	*/
	
	
	/*
	 * @param savedPath 存入ftp服务器的路径
	 * @param localfile 本地文件
	 * @param savedFileName 保存在ftp服务器的名字
	 * */
	public String uploadFile(File localFile, String savedDir1, String savedDir2, String savedDir3, String savedDir4, String savedFileName) {
		FileInputStream fis;  //输入文件流
		boolean flag = false;
		try {
			fis = new FileInputStream(localFile);	//建立文件流
			FTPClient ftpClient = new FTPClient();	//建立ftp客户端
			ftpClient.connect(host, port);	//设置连接主机ip和port
			ftpClient.login(username, password);	//设置用户名和密码
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	//设置ftp传输文件类型
			ftpClient.enterLocalPassiveMode();	//采取被动传输模式
			ftpClient.setDataTimeout(defaultTimeout); 	//设置默认传输超时时间
			ftpClient.setBufferSize(1024*1024);	//设置缓冲区1M
			//while(!JudgeHasFileDir(ftpClient, savedDir1));	//针对性的优化
			ftpClient.changeWorkingDirectory(savedDir1);
			while(!JudgeHasFileDir(ftpClient, savedDir2));	//如果ftp服务器下不存在指定的文件夹，就一直循环
			ftpClient.changeWorkingDirectory(savedDir2);
			while(!JudgeHasFileDir(ftpClient, savedDir3));	//如果ftp服务器下不存在指定的文件夹，就一直循环
			ftpClient.changeWorkingDirectory(savedDir3);
			while(!JudgeHasFileDir(ftpClient, savedDir4));	//如果ftp服务器下不存在指定的文件夹，就一直循环
		    ftpClient.changeWorkingDirectory(savedDir4);
		    //因为FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码
		    if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
		    	// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
		    	LOCAL_CHARSET = "UTF-8";
		    }
		    ftpClient.setControlEncoding(LOCAL_CHARSET);
			flag  = ftpClient.storeFile(new String(savedFileName.getBytes(LOCAL_CHARSET), FTP_REQURED_CHARSET), fis);
			fis.close();
			//关闭ftp连接
			ftpClient.logout();
			ftpClient.disconnect();
			
		} catch (SocketException e) {
			e.printStackTrace();
			return "SOCKET NOT SUCCESS";
		} catch(IOException e) {
			e.printStackTrace();
			return "IO NOT SUCCESS";
		}
		if(flag) {
			return "SUCCESS";
		} else {
			return "NOT SUCCESS";
		}
	}
	
	
	
	/*
	 * 判断是否 存在指定的文件夹
	 * @param fileDirName 文件夹名字
	 * */
	public boolean JudgeHasFileDir(FTPClient ftpClient, String FileDirName) {
		try {
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for(FTPFile file : ftpFiles) {
				if(file.isDirectory()) {
					if(file.getName().equals(FileDirName)) return true;
					else {
						ftpClient.changeWorkingDirectory(file.getName());
						JudgeHasFileDir(ftpClient, FileDirName);
					}
				} else {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * test
	 * */
	public static void main(String[] args) {
		String fileName = "";  //本地完整的文件绝对路径
		//String savedPath = "";  //ftp保存的文件夹
		//下面是保存在FTP服务器上的路径的四级目录
		String savedDir1 = "";
		String savedDir2 = "";
		String savedDir3 = "";
		String savedDir4 = "";
		String savedFileName = ""; //ftp保存的文件的名字
		//命令行初始化参数
		if(args.length == 6) {
			fileName = args[0];
			savedDir1 = args[1];
			savedDir2 = args[2];
			savedDir3 = args[3];
			savedDir4 = args[4];
			savedFileName = args[5];
		} else {
			System.out.println("请输入指定的全部参数 fileName savedPath savedFileName");
			return;
		}
		
		//建立ftp客户端
		FTPhelper ftPhelper = new FTPhelper();
		File file = new File(fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long begin_time = System.currentTimeMillis();
		String status = ftPhelper.uploadFile(file, savedDir1, savedDir2, savedDir3, savedDir4,savedFileName);
		long end_time = System.currentTimeMillis();
		System.out.println(status);
		System.out.println("program run time is: " + (end_time-begin_time) + " ms");
	}
	
}
