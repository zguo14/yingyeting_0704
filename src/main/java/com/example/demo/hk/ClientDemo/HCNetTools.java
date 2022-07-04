package com.example.demo.hk.ClientDemo;


import cc.eguid.FFmpegCommandManager.FFmpegManager;
import com.example.demo.hk.dao.entity.*;
//import com.example.demo.hk.entity.*;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

public class HCNetTools {
	private Logger logger = LoggerFactory.getLogger(getClass());
	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	static PlayCtrl playControl = PlayCtrl.INSTANCE;

	HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;//设备信息
	HCNetSDK.NET_DVR_IPPARACFG m_strIpparaCfg;//IP参数
	HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;//用户参数

	boolean bRealPlay;//是否在预览.
	String m_sDeviceIP;//已登录设备的IP地址

	NativeLong lUserID;//用户句柄
	NativeLong loadHandle;//下载句柄
	NativeLong lPreviewHandle;//预览句柄
	static NativeLongByReference m_lPort = new NativeLongByReference(new NativeLong(-1));
	static FRealDataCallBack fRealDataCallBack = new FRealDataCallBack();

	FFmpegManager manager;//rstp转rmtp工具

	String line = "";
	private Process process;
	// ffmpeg位置，最好写在配置文件中
	String ffmpegPath = "C:\\Users\\Zihao\\Desktop\\yingyeting\\ffmpeg-4.3.2-2021-02-27-essentials_build\\bin\\";

	//FRealDataCallBack fRealDataCallBack;//预览回调函数实现
	public HCNetTools() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);//防止被播放窗口(AWT组件)覆盖
		lUserID = new NativeLong(-1);
		lPreviewHandle = new NativeLong(-1);
//		m_lPort = new NativeLongByReference(new NativeLong(-1));
		//fRealDataCallBack= new FRealDataCallBack();
	}

	/**
	 * 初始化资源配置
	 */
	public int initDevices() {
		boolean b = hCNetSDK.NET_DVR_Init();
		if (!b) {
			//初始化失败
			//throw new RuntimeException("初始化失败");
			System.out.println("初始化失败");
			return 1;
		}
		return 0;
	}

	/**
	 * 设备注册
	 *
	 * @param name     设备用户名
	 * @param password 设备登录密码
	 * @param ip       IP地址
	 * @param port     端口
	 * @return 结果
	 */
	public int deviceRegist(String name, String password, String ip, String port) {

		if (bRealPlay) {//判断当前是否在预览
			return 2;//"注册新用户请先停止当前预览";
		}
		if (lUserID.longValue() > -1) {//先注销,在登录
			hCNetSDK.NET_DVR_Logout_V30(lUserID);
			lUserID = new NativeLong(-1);
		}
		//注册(既登录设备)开始
		m_sDeviceIP = ip;
		short iPort = Integer.valueOf(port).shortValue();
		m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();//获取设备参数结构
		lUserID = hCNetSDK.NET_DVR_Login_V30(ip, iPort, name, password, m_strDeviceInfo);//登录设备
		long userID = lUserID.longValue();
		if (userID == -1) {
			int iErr = hCNetSDK.NET_DVR_GetLastError();
			System.out.println("：注册失败,错误号：" + iErr);
			System.out.println(hCNetSDK.NET_DVR_GetErrorMsg(m_lPort));
			m_sDeviceIP = "";//登录未成功,IP置为空
			return 3;//"注册失败";
		}
		return 0;
	}

	/**
	 * 获取设备通道
	 */
	public int getChannelNumber() {
		IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
		boolean bRet = false;
		int iChannelNum = -1;

		m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
		m_strIpparaCfg.write();
		Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
		bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0), lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
		m_strIpparaCfg.read();

		String devices = "";
		if (!bRet) {
			//设备不支持,则表示没有IP通道
			for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++) {
				devices = "Camera" + (iChannum + m_strDeviceInfo.byStartChan);
			}
		} else {
			for (int iChannum = 0; iChannum < HCNetSDK.MAX_IP_CHANNEL; iChannum++) {
				if (m_strIpparaCfg.struIPChanInfo[iChannum].byEnable == 1) {
					devices = "IPCamera" + (iChannum + m_strDeviceInfo.byStartChan);
				}
			}
		}
		if (StringUtils.isNotEmpty(devices)) {
			if (devices.charAt(0) == 'C') {//Camara开头表示模拟通道
				//子字符串中获取通道号
				iChannelNum = Integer.parseInt(devices.substring(6));
			} else {
				if (devices.charAt(0) == 'I') {//IPCamara开头表示IP通道
					//子字符创中获取通道号,IP通道号要加32
					iChannelNum = Integer.parseInt(devices.substring(8)) + 32;
				} else {
					return 4;
				}
			}
		}
		return iChannelNum;
	}

	public void shutDownDev() {
		//如果已经注册,注销
		if (lUserID.longValue() > -1) {
			hCNetSDK.NET_DVR_Logout_V30(lUserID);
		}
		hCNetSDK.NET_DVR_Cleanup();
	}

	/**
	 * 抓拍图片
	 *
	 * @param
	 */
	public int getDVRPic(CapturePicRequestParam param, NativeLong lRealHandle) {

		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String filePath = param.getPath() + param.getName() + "-" + sdf.format(date) + ".jpg";
		logger.info("filePath:" + filePath);

		boolean is = hCNetSDK.NET_DVR_CapturePicture(lRealHandle, filePath);
		if (is) {
			logger.info("hksdk(抓图)-结果状态值(0表示成功):" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info("hksdk(抓图)-抓取失败,错误码:" + hCNetSDK.NET_DVR_GetLastError());
			return -4;
		}

		return 1;
	}

	public List<NativeLong> initDVR(CapturePicRequestParam param) {

		List<NativeLong> resp = new ArrayList<>();

		if (!hCNetSDK.NET_DVR_Init()) {
			logger.warn("hksdk(抓图)-海康sdk初始化失败!");
			resp.add(new NativeLong(-1));
			return resp;
		}

		HCNetSDK.NET_DVR_DEVICEINFO_V30 devinfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();// 设备信息
//		param.setIp("10.116.198.21");
//		param.setIp("10.117.232.190");
		param.setPort("8000");
		logger.info(param.getIp() + ":" + param.getPort());
		hCNetSDK.NET_DVR_SetLogToFile(3, "C:/SdkLog", false);

		lUserID = hCNetSDK.NET_DVR_Login_V30(param.getIp(), Short.valueOf(param.getPort()), param.getAccount(), param.getPassword(), devinfo);// 返回一个用户编号，同时将设备信息写入devinfo
		if (lUserID.intValue() < 0) {
			logger.warn("hksdk(抓图)-设备注册失败,错误码:" + hCNetSDK.NET_DVR_GetLastError());
			resp.add(new NativeLong(-2));
			return resp;
		}
		resp.add(lUserID);

		HCNetSDK.NET_DVR_WORKSTATE_V30 devwork = new HCNetSDK.NET_DVR_WORKSTATE_V30();
		if (!hCNetSDK.NET_DVR_GetDVRWorkState_V30(lUserID, devwork)) {
			// 返回Boolean值，判断是否获取设备能力
			logger.info("hksdk(抓图)-返回设备状态失败");
			resp.add(new NativeLong(-3));
			return resp;
		}

		HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
		m_strClientInfo.lChannel = new NativeLong(1);// 设置通道
		m_strClientInfo.hPlayWnd = null;

		NativeLong lRealHandle = hCNetSDK.NET_DVR_RealPlay_V30(lUserID, m_strClientInfo, fRealDataCallBack, null, true);
		logger.info("lRealHandle:" + lRealHandle);
		if (lRealHandle.intValue() != 0) {
			logger.info("hksdk(抓图)-预览失败");
			resp.add(new NativeLong(-4));
			return resp;
		}
		resp.add(lRealHandle);

		return resp;
	}

	public boolean resetDVR(List<NativeLong> handle) {

		boolean flag1 = hCNetSDK.NET_DVR_StopRealPlay(handle.get(1)); // 停止预览
		boolean flag2 = hCNetSDK.NET_DVR_Logout(handle.get(0)); // 用户退出登录

		return flag1 && flag2;

	}


	/**
	 * FFmpeg抓拍图片
	 *
	 * @param
	 */
	public int getDVRPicByFFmpeg(CapturePicRequestParam param, NativeLong lRealHandle) {
		try {
			String command = ffmpegPath;
			command += "ffmpeg -y -i rtsp://";
			command += param.getAccount() + ":" + param.getPassword() + "@" + param.getIp() + ":" + param.getPort();
			command += "/h264/ch1/main/av_stream";
			command += " -vframes 1 ";
			Date date=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String filePath = param.getPath() + param.getName() + "-" + sdf.format(date) + ".jpg";
			command += filePath;
			System.out.println("ffmpeg截图命令：" + command);
			// 运行cmd命令，获取其进程
			process = Runtime.getRuntime().exec(command);
			// 输出控制台日志
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while((line = br.readLine()) != null) {
				System.out.println("截图信息[" + line + "]");
			}
			if(process != null) {
				process.destroy();
			}
			System.out.println("销毁截图进程");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 1;
	}

	/* *
	 * @Description:  下载录像
	 * @param null
	 * @Return：
	 */
	public boolean downloadVideo(Device dvr, Date startTime, Date endTime, String filePath, int channel) {
		boolean initFlag = hCNetSDK.NET_DVR_Init();
		if (!initFlag) { //返回值为布尔值 fasle初始化失败
			logger.warn("hksdk(视频)-海康sdk初始化失败!");
			return false;
		}
		HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
		lUserID = hCNetSDK.NET_DVR_Login_V30(dvr.getIp(), Short.valueOf(dvr.getPort()), dvr.getAccount(), dvr.getPassword(), deviceInfo);
		logger.info("hksdk(视频)-登录海康录像机信息,状态值:" + hCNetSDK.NET_DVR_GetLastError());
		long lUserId = lUserID.longValue();
		if (lUserId == -1) {
			logger.warn("hksdk(视频)-海康sdk登录失败!");
			return false;
		}
		loadHandle = new NativeLong(-1);
		if (loadHandle.intValue() == -1) {
			loadHandle = hCNetSDK.NET_DVR_GetFileByTime(lUserID, new NativeLong(channel), getHkTime(startTime), getHkTime(endTime), filePath);
			logger.info("hksdk(视频)-获取播放句柄信息,状态值:" + hCNetSDK.NET_DVR_GetLastError());
			if (loadHandle.intValue() >= 0) {
				boolean downloadFlag = hCNetSDK.NET_DVR_PlayBackControl(loadHandle, hCNetSDK.NET_DVR_PLAYSTART, 0, null);
				int tmp = -1;
				IntByReference pos = new IntByReference();
				while (true) {
					boolean backFlag = hCNetSDK.NET_DVR_PlayBackControl(loadHandle, hCNetSDK.NET_DVR_PLAYGETPOS, 0, pos);
					if (!backFlag) {//防止单个线程死循环
						return downloadFlag;
					}
					int produce = pos.getValue();
					if ((produce % 10) == 0 && tmp != produce) {//输出进度
						tmp = produce;
						logger.info("hksdk(视频)-视频下载进度:" + "==" + produce + "%");
					}
					if (produce == 100) {//下载成功
						hCNetSDK.NET_DVR_StopGetFile(loadHandle);
						loadHandle.setValue(-1);
						hCNetSDK.NET_DVR_Logout(lUserID);//退出录像机
						logger.info("hksdk(视频)-退出状态" + hCNetSDK.NET_DVR_GetLastError());
						//hcNetSDK.NET_DVR_Cleanup();
						return true;
					}
					if (produce > 100) {//下载失败
						hCNetSDK.NET_DVR_StopGetFile(loadHandle);
						loadHandle.setValue(-1);
						logger.warn("hksdk(视频)-海康sdk由于网络原因或DVR忙,下载异常终止!错误原因:" + hCNetSDK.NET_DVR_GetLastError());
						//hcNetSDK.NET_DVR_Logout(userId);//退出录像机
						//logger.info("hksdk(视频)-退出状态"+hcNetSDK.NET_DVR_GetLastError());
						return false;
					}
				}
			} else {
				System.out.println("hksdk(视频)-下载失败" + hCNetSDK.NET_DVR_GetLastError());
				return false;
			}
		}
		return false;
	}

	public boolean downloadCurrentVideo_SDK(Device dvr, String filePath) {
		//初始化
		boolean initFlag = hCNetSDK.NET_DVR_Init();
		if (!initFlag) { //返回值为布尔值 fasle初始化失败
			logger.warn("hksdk(视频)-海康sdk初始化失败!");
			return false;
		}
		//通道
//		NativeLong channel = new NativeLong(1);
//		dvr.setChannel(channel);

		//设备登录
		HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
		lUserID = hCNetSDK.NET_DVR_Login_V30(dvr.getIp(), Short.valueOf(dvr.getPort()), dvr.getAccount(), dvr.getPassword(), deviceInfo);
		if (lUserID.longValue() == -1) {
			logger.warn("hksdk(视频)-海康sdk登录失败!");
			return false;
		} else {
			dvr.setUserId(lUserID);
			logger.info("hksdk(视频)-登录海康录像机信息,状态值:" + hCNetSDK.NET_DVR_GetLastError());
			System.out.println("登录成功" );
		}

		HCNetSDK.NET_DVR_WORKSTATE_V30 devwork = new HCNetSDK.NET_DVR_WORKSTATE_V30();
		if(!hCNetSDK.NET_DVR_GetDVRWorkState_V30(dvr.getUserId(), devwork)){
			System.out.println("返回设备状态失败");
			return false;
		} else {
			System.out.println("设备状态：" + devwork.dwDeviceStatic);// 0正常，1CPU占用率过高，2硬件错误，3未知
		}

		//判断是否获取到设备能力
		HCNetSDK.NET_DVR_WORKSTATE_V30 devWork = new HCNetSDK.NET_DVR_WORKSTATE_V30();
		if(!hCNetSDK.NET_DVR_GetDVRWorkState_V30(dvr.getUserId(), devWork)){
			System.out.println("获取设备能力集失败,返回设备状态失败...............");
		}

		//启动实时预览功能  创建clientInfo对象赋值预览参数
		HCNetSDK.NET_DVR_CLIENTINFO clientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
		clientInfo.lChannel=dvr.getChannel();   //设置通道号
		clientInfo.lLinkMode = new NativeLong(3);  //RTP取流
		clientInfo.sMultiCastIP=null;                   //不启动多播模式
		//创建窗口句柄
		clientInfo.hPlayWnd=null;

//		HCNetSDK.NET_DVR_PREVIEWINFO previewInfo = new HCNetSDK.NET_DVR_PREVIEWINFO();
//		previewInfo.hPlayWnd     = null;  // 仅取流不解码。这是Linux写法，Windows写法是struPlayInfo.hPlayWnd = NULL;
//		previewInfo.lChannel     = new NativeLong(1); // 通道号
//		previewInfo.dwStreamType = new NativeLong(0);  // 0- 主码流，1-子码流，2-码流3，3-码流4，以此类推
//		previewInfo.dwLinkMode   = new NativeLong(0);  // 0- TCP方式，1- UDP方式，2- 多播方式，3- RTP方式，4-RTP/RTSP，5-RSTP/HTTP
//		previewInfo.bBlocked     = true;  // 0- 非阻塞取流，1- 阻塞取流
		//struPlayInfo.dwDisplayBufNum = 1;

//		ClientDemo clientDemo = new ClientDemo();
//		ClientDemo.FRealDataCallBack fRealDataCallBack = clientDemo.fRealDataCallBack;

		//开启实时预览
		NativeLong key = hCNetSDK.NET_DVR_RealPlay_V30(dvr.getUserId(), clientInfo, null, null, false);
//		NativeLong key = hCNetSDK.NET_DVR_RealPlay_V40(dvr.getUserId(), previewInfo, fRealDataCallBack, null);
		if(key.intValue()==-1){
			System.out.println("预览失败   错误代码为:  " + hCNetSDK.NET_DVR_GetLastError());
			hCNetSDK.NET_DVR_Logout(dvr.getUserId());
			hCNetSDK.NET_DVR_Cleanup();
			return false;
		} else {
			System.out.println("预览成功 返回码为: " + key.intValue());
			System.out.println("预览成功 上一个错误吗为: " + hCNetSDK.NET_DVR_GetLastError());
		}

		// 如果没有文件则创建 保存在 D://realData/result.mp4 中
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
		System.out.println("创建的文件： " + file.getAbsolutePath());
		//预览成功后 调用接口使视频资源保存到文件中
		hCNetSDK.NET_DVR_SaveRealData(key, file.getAbsolutePath()+ "\\result1.mp4");
//		if(!hCNetSDK.NET_DVR_SaveRealData(key, file.getAbsolutePath()+ "\\result1.mp4")){
////		if(!hCNetSDK.NET_DVR_SaveRealData(key, filePath + "\\result.mp4")){
//			System.out.println("保存到文件失败 错误码为:  " + hCNetSDK.NET_DVR_GetLastError());
//			hCNetSDK.NET_DVR_StopRealPlay(key);
//			hCNetSDK.NET_DVR_Logout(dvr.getUserId());
//			hCNetSDK.NET_DVR_Cleanup();
//		} else {
//			System.out.println("保存文件成功");
//		}

		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//上面设置的睡眠时间可以当做拍摄时长来使用,然后调用结束预览,注销用户,释放资源就可以了
		hCNetSDK.NET_DVR_StopRealPlay(key);
		hCNetSDK.NET_DVR_Logout(dvr.getUserId());
		hCNetSDK.NET_DVR_Cleanup();
		// 程序运行完毕退出阻塞状态
//		System.exit(0);

		return true;

	}


	//调用ffmpeg拉流
	public int downloadCurrentVideo (DownloadVideoRequestParam param, Camera camera, Long start, Long end, int gap) {
//		int second = (int) (Long.valueOf(param.getBeginning()) - System.currentTimeMillis()/1000L);
//		System.out.println("start to sleep: " + second + " seconds");
//		if (second > 0) {
//			try {
//				Thread.sleep( second * 1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

		if (gap > 0) {
			System.out.println("start to sleep: " + gap + " seconds");
			try {
				Thread.sleep( gap * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("awake");
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			// cmd命令拼接
			String command = ffmpegPath;
			command += "ffmpeg -i rtsp://";
//			command += param.getAccount() + ":" + param.getPassword() + "@" + param.getIp() + ":" + param.getPort();
			command += "admin" + ":" + "a123456789" + "@" + camera.getCameraIp() + ":" + camera.getCameraPort();
			command += "/h264/ch1/main/av_stream";
			command += " -t " + param.getDuration();
			command += " C:\\Users\\Zihao\\Desktop\\yingyeting\\file\\";
//			command += param.getName() + "-" + sdf.format(date) + ".mp4";
//			System.out.println(param.getName() + "-" + sdf.format(date) + ".mp4");
			command += camera.getCameraName() + "-" + sdf.format(date) + ".mp4";
			System.out.println(camera.getCameraName() + "-" + sdf.format(date) + ".mp4");
			System.out.println("ffmpeg拉流：" + command);
			// 运行cmd命令，获取其进程
			process = Runtime.getRuntime().exec(command);
			Date date3 = new Date();
			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-ms");
			System.out.println("开始拉流 时间 ： " + sdf3.format(date3));
			// 输出控制台日志
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while((line = br.readLine()) != null) {
				System.out.println("拉流信息[" + line + "]");
			}
			if(process != null) {
				process.destroy();
			}
			System.out.println("销毁拉流进程");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public int getStatus(GetStatusRequestParam param) {
		//初始化
		boolean initFlag = hCNetSDK.NET_DVR_Init();
		if (!initFlag) { //返回值为布尔值 fasle初始化失败
			logger.warn("hksdk(视频)-海康sdk初始化失败!");
			return -1;
		}
		//设备登录
		HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
		lUserID = hCNetSDK.NET_DVR_Login_V30(param.getIp(), Short.valueOf(param.getPort()), param.getAccount(), param.getPassword(), deviceInfo);
		if (lUserID.longValue() == -1) {
			logger.warn("hksdk(视频)-海康sdk登录失败!");
			return -2;
		} else {
			param.setUserId(lUserID);
			logger.info("hksdk(视频)-登录海康录像机信息,状态值:" + hCNetSDK.NET_DVR_GetLastError());
			System.out.println("登录成功" );
		}

		HCNetSDK.NET_DVR_WORKSTATE_V30 devwork = new HCNetSDK.NET_DVR_WORKSTATE_V30();
		if(!hCNetSDK.NET_DVR_GetDVRWorkState_V30(param.getUserId(), devwork)){
			System.out.println("返回设备状态失败");
			return -3;
		} else {
			return devwork.dwDeviceStatic; // 0正常，1CPU占用率过高，2硬件错误，3未知
		}
	}

	/* *
	 * @Description:  获取录像文件信息
	 * @param null
	 * @Return：
	 */
	public List getFileList(Device dvr, Date startTime, Date endTime, int channel) {
		List fileList = new ArrayList();
		boolean initFlag = hCNetSDK.NET_DVR_Init();
		if (!initFlag) { //返回值为布尔值 fasle初始化失败
			logger.warn("hksdk(视频)-海康sdk初始化失败!");
			//return null;
		}
		HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
		lUserID = hCNetSDK.NET_DVR_Login_V30(dvr.getIp(), Short.valueOf(dvr.getPort()), dvr.getAccount(), dvr.getPassword(), deviceInfo);
		logger.info("hksdk(视频)-登录海康录像机信息,状态值:" + hCNetSDK.NET_DVR_GetLastError());
		long lUserId = lUserID.longValue();
		if (lUserId == -1) {
			logger.warn("hksdk(视频)-海康sdk登录失败!");
			//return null;
		}
		// 搜索条件
		HCNetSDK.NET_DVR_FILECOND m_strFilecond = new HCNetSDK.NET_DVR_FILECOND();
		m_strFilecond.struStartTime = getHkTime(startTime);
		m_strFilecond.struStopTime = getHkTime(endTime);
		m_strFilecond.lChannel = new NativeLong(channel);//通道号

		NativeLong lFindFile = hCNetSDK.NET_DVR_FindFile_V30(lUserID, m_strFilecond);
		HCNetSDK.NET_DVR_FINDDATA_V30 strFile = new HCNetSDK.NET_DVR_FINDDATA_V30();
		long findFile = lFindFile.longValue();
		if (findFile > -1) {
			System.out.println("file" + findFile);
		}
		NativeLong lnext;
		strFile = new HCNetSDK.NET_DVR_FINDDATA_V30();
		Map map = null;
		boolean flag=true;
		while (flag) {
			lnext = hCNetSDK.NET_DVR_FindNextFile_V30(lFindFile, strFile);
			if (lnext.longValue() == HCNetSDK.NET_DVR_FILE_SUCCESS) {
				//搜索成功
				map = new HashMap<>();
				//添加文件名信息
				String[] s = new String[2];
				s = new String(strFile.sFileName).split("\0", 2);
				map.put("fileName", new String(s[0]));

				int iTemp;
				String MyString;
				if (strFile.dwFileSize < 1024 * 1024) {
					iTemp = (strFile.dwFileSize) / (1024);
					MyString = iTemp + "K";
				} else {
					iTemp = (strFile.dwFileSize) / (1024 * 1024);
					MyString = iTemp + "M   ";
					iTemp = ((strFile.dwFileSize) % (1024 * 1024)) / (1204);
					MyString = MyString + iTemp + "K";
				}
				map.put("fileSize", MyString);                      //添加文件大小信息
				map.put("struStartTime", strFile.struStartTime.toStringTime());                      //添加开始时间信息
				map.put("struStopTime", strFile.struStopTime.toStringTime());                      //添加结束时间信息
				fileList.add(map);
			}else
			{
				if (lnext.longValue() == HCNetSDK.NET_DVR_ISFINDING)
				{//搜索中
					//System.out.println("搜索中");
					continue;
				}
				else
				{
					flag=false;
					if (lnext.longValue() == HCNetSDK.NET_DVR_FILE_NOFIND)
					{
						//flag=false;
					}
					else
					{
						//flag=false;
						System.out.println("搜索文件结束");
						boolean flag2 = hCNetSDK.NET_DVR_FindClose_V30(lFindFile);
						if (flag2 == false)
						{
							System.out.println("结束搜索失败");
						}
					}
				}
			}
		}
		return fileList;
	}

	/**
	 * 获取海康录像机格式的时间
	 *
	 * @param time
	 * @return
	 */
	public HCNetSDK.NET_DVR_TIME getHkTime(Date time) {
		HCNetSDK.NET_DVR_TIME structTime = new HCNetSDK.NET_DVR_TIME();
		String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
		String[] times = str.split("-");
		structTime.dwYear = Integer.parseInt(times[0]);
		structTime.dwMonth = Integer.parseInt(times[1]);
		structTime.dwDay = Integer.parseInt(times[2]);
		structTime.dwHour = Integer.parseInt(times[3]);
		structTime.dwMinute = Integer.parseInt(times[4]);
		structTime.dwSecond = Integer.parseInt(times[5]);
		return structTime;
	}

	// 暂时先不传null
	static class FRealDataCallBack implements HCNetSDK.FRealDataCallBack_V30 {
		@Override
		public void invoke(NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) {
			switch (dwDataType) {
				case HCNetSDK.NET_DVR_SYSHEAD: //系统头
					if (!playControl.PlayM4_GetPort(m_lPort)) //获取播放库未使用的通道号
					{
						break;
					}
					if (dwBufSize > 0) {
						if (!playControl.PlayM4_SetStreamOpenMode(m_lPort.getValue(), PlayCtrl.STREAME_REALTIME))  //设置实时流播放模式
						{
							break;
						}
						if (!playControl.PlayM4_OpenStream(m_lPort.getValue(), pBuffer, dwBufSize, 1024 * 1024)) //打开流接口
						{
							break;
						}
						if (!playControl.PlayM4_Play(m_lPort.getValue(), null)) //播放开始
						{
							break;
						}

					}
				case HCNetSDK.NET_DVR_STREAMDATA:   //码流数据
					if ((dwBufSize > 0) && (m_lPort.getValue().intValue() != -1)) {
						if (!playControl.PlayM4_InputData(m_lPort.getValue(), pBuffer, dwBufSize))  //输入流数据
						{
							break;
						}
					}
			}
		}
	}


}
