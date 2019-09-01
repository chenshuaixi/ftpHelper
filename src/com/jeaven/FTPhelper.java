package com.jeaven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPhelper {
	//����ftp�Ĳ���
	public static String host;
	public static int port;
	public static String username;
	public static String password;
	public static String encode;
	public static int defaultTimeout;
	
	//����ftp�Ĳ���
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
	 * @param savedPath ����ftp��������·��
	 * @param localfile �����ļ�
	 * @param savedFileName ������ftp������������
	 * */
	public String uploadFile(String savedPath, File localFile, String savedFileName) {
		FileInputStream fis;  //�����ļ���
		boolean flag = false;
		try {
			fis = new FileInputStream(localFile);	//�����ļ���
			FTPClient ftpClient = new FTPClient();	//����ftp�ͻ���
			ftpClient.setControlEncoding(encode);	//���ñ���
			ftpClient.connect(host, port);	//������������ip��port
			ftpClient.login(username, password);	//�����û���������
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	//����ftp�����ļ�����
			ftpClient.enterLocalPassiveMode();	//��ȡ��������ģʽ
			ftpClient.setDataTimeout(defaultTimeout); 	//����Ĭ�ϴ��䳬ʱʱ��
			ftpClient.setBufferSize(1024*1024);	//���û�����1M
			//�ж��Ƿ����ָ�����ļ�·��
			//if(!JudgeHasFileDir(ftpClient, savedPath)) return "������ָ�����ļ���·��";
			while(!JudgeHasFileDir(ftpClient, savedPath));	//���ftp�������²�����ָ�����ļ��У���һֱѭ��
			//�л�����Ŀ¼�������·�����棬��������ھʹ���һ��Ŀ¼
			boolean isFileDirExisted = ftpClient.changeWorkingDirectory(savedPath);
			if(isFileDirExisted == false) {   //��������ھʹ���һ��·�����л�����Ŀ¼
				ftpClient.makeDirectory(savedPath);
				ftpClient.changeWorkingDirectory(savedPath);
			}
			flag = ftpClient.storeFile(savedFileName, fis);  //�ϴ�ftp
			fis.close();
			//�ر�ftp����
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
	 * �ж��Ƿ� ����ָ�����ļ���
	 * @param fileDirName �ļ�������
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
		String fileName = "";  //�����������ļ�����·��
		String savedPath = "";  //ftp������ļ���
		String savedFileName = ""; //ftp������ļ�������
		//�����г�ʼ������
		if(args.length > 0) {
			if(args[0] != null) {
				 fileName = args[0];
			} else {
				System.out.println("ȱ�ٲ���  ���������  fileName");
				return;
			}
			if(args[1] != null) {
				savedPath = args[1];
			} else {
				System.out.println("ȱ�ٲ��� ���������  savedPath");
				return;
			}
			if(args[2] != null) {
				savedFileName = args[2];
			} else {
				System.out.println("ȱ�ٲ��� ���������  savedFileName");
				return;
			}
		} else {
			System.out.println("������ָ���Ĳ��� fileName savedPath savedFileName");
			return;
		}
		
		//����ftp�ͻ���
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
