package com.jeaven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPhelper {
	//����ftp�Ĳ���
	public static String host;
	public static int port;
	public static String username;
	public static String password;
	public static int defaultTimeout;
	public static String LOCAL_CHARSET;
	public static String FTP_REQURED_CHARSET;
	
	//����ftp�Ĳ���
	static {
		try {  
			host = "202.38.246.199";
			port = 2100;
			username = "wang_lin_ge";
			password = "123456";
			defaultTimeout = 3600000;  //���ó�ʱʱ��Ϊ1Сʱ
			LOCAL_CHARSET = "GBK";  //��ʼĬ�ϵı����Ǳ��ص�GBK����
			FTP_REQURED_CHARSET = "iso-8859-1";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * @param savedPath ����ftp��������·��
	 * @param localfile �����ļ�
	 * @param savedFileName ������ftp������������
	 * */
	/*
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
			//����Ե������Ż�
			ftpClient.changeWorkingDirectory("booking");
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
	*/
	
	
	/*
	 * @param savedPath ����ftp��������·��
	 * @param localfile �����ļ�
	 * @param savedFileName ������ftp������������
	 * */
	public String uploadFile(File localFile, String savedDir1, String savedDir2, String savedDir3, String savedDir4, String savedFileName) {
		FileInputStream fis;  //�����ļ���
		boolean flag = false;
		try {
			fis = new FileInputStream(localFile);	//�����ļ���
			FTPClient ftpClient = new FTPClient();	//����ftp�ͻ���
			ftpClient.connect(host, port);	//������������ip��port
			ftpClient.login(username, password);	//�����û���������
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	//����ftp�����ļ�����
			ftpClient.enterLocalPassiveMode();	//��ȡ��������ģʽ
			ftpClient.setDataTimeout(defaultTimeout); 	//����Ĭ�ϴ��䳬ʱʱ��
			ftpClient.setBufferSize(1024*1024);	//���û�����1M
			//while(!JudgeHasFileDir(ftpClient, savedDir1));	//����Ե��Ż�
			ftpClient.changeWorkingDirectory(savedDir1);
			while(!JudgeHasFileDir(ftpClient, savedDir2));	//���ftp�������²�����ָ�����ļ��У���һֱѭ��
			ftpClient.changeWorkingDirectory(savedDir2);
			while(!JudgeHasFileDir(ftpClient, savedDir3));	//���ftp�������²�����ָ�����ļ��У���һֱѭ��
			ftpClient.changeWorkingDirectory(savedDir3);
			while(!JudgeHasFileDir(ftpClient, savedDir4));	//���ftp�������²�����ָ�����ļ��У���һֱѭ��
		    ftpClient.changeWorkingDirectory(savedDir4);
		    //��ΪFTPЭ�����棬�涨�ļ�������Ϊiso-8859-1������Ŀ¼�����ļ�����Ҫת��
		    if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
		    	// ������������UTF-8��֧�֣����������֧�־���UTF-8���룬�����ʹ�ñ��ر��루GBK��.
		    	LOCAL_CHARSET = "UTF-8";
		    }
		    ftpClient.setControlEncoding(LOCAL_CHARSET);
			flag  = ftpClient.storeFile(new String(savedFileName.getBytes(LOCAL_CHARSET), FTP_REQURED_CHARSET), fis);
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
		//String savedPath = "";  //ftp������ļ���
		//�����Ǳ�����FTP�������ϵ�·�����ļ�Ŀ¼
		String savedDir1 = "";
		String savedDir2 = "";
		String savedDir3 = "";
		String savedDir4 = "";
		String savedFileName = ""; //ftp������ļ�������
		//�����г�ʼ������
		if(args.length == 6) {
			fileName = args[0];
			savedDir1 = args[1];
			savedDir2 = args[2];
			savedDir3 = args[3];
			savedDir4 = args[4];
			savedFileName = args[5];
		} else {
			System.out.println("������ָ����ȫ������ fileName savedPath savedFileName");
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
		long begin_time = System.currentTimeMillis();
		String status = ftPhelper.uploadFile(file, savedDir1, savedDir2, savedDir3, savedDir4,savedFileName);
		long end_time = System.currentTimeMillis();
		System.out.println(status);
		System.out.println("program run time is: " + (end_time-begin_time) + " ms");
	}
	
}
