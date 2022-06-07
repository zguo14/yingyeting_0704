package com.example.demo.hk.entity;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import org.springframework.beans.factory.annotation.Autowired;

public class Device {

	/*device.getType() 代表摄像头类型 0:华为 1:华康
  device.getAccount() 获取设备的访问账号
  device.getPassword() 获取设备的访问密码
  device.getIp() 获取设备所在内网的IP地址
  device.getPort() 获取设备的访问端口 rtsp默认554*/

	private String account;
	private String password;
	private String ip;
	private String port;
	private String name; //营业厅名称
	private NativeLong userId;
	private NativeLong channel;


	public String getAccount() {
		return account;
	}
	public String getName() {
		return name;
	}
	public void setChannel (NativeLong channel) { this.channel = channel; }
	public NativeLong getChannel () { return this.channel; }
	public void setUserId(NativeLong userId) { this.userId = userId; }
	public NativeLong getUserId() { return userId; }

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
