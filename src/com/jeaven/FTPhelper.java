package com.jeaven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPhelper {
	//定义ftp的参数
	public static String host;
	public static int port;
	public static String username;
	public static String password;
	public static String encode;
	public static int defaultTimeout;
	
	//配置ftp的参数
	static {
		try {  
			host = "127.0.0.1";
			port = 8888;
			username = "ftpUser";
			password = "123456";
			encode = "UTF-8";
			defaultTimeout = 30000;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * @param savedPath 存入ftp服务器的路径
	 * @param localfile 本地文件
	 * @param savedFileName 保存在ftp服务器的名字
	 * */
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
		String savedPath = "";  //ftp保存的文件夹
		String savedFileName = ""; //ftp保存的文件的名字
		//命令行初始化参数
		if(args.length > 0) {
			if(args[0] != null) {
				 fileName = args[0];
			} else {
				System.out.println("缺少参数  请输入参数  fileName");
				return;
			}
			if(args[1] != null) {
				savedPath = args[1];
			} else {
				System.out.println("缺少参数 请输入参数  savedPath");
				return;
			}
			if(args[2] != null) {
				savedFileName = args[2];
			} else {
				System.out.println("缺少参数 请输入参数  savedFileName");
				return;
			}
		} else {
			System.out.println("请输入指定的参数 fileName savedPath savedFileName");
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
		String status = ftPhelper.uploadFile(savedPath, file, savedFileName);
		System.out.println(status);
	}
	
}
