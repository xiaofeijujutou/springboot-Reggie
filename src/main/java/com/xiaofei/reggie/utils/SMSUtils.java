package com.xiaofei.reggie.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 * LTAI5t7z6mkfF5UFuPMcF9EZ
	 * YamClltNMdX0cknBwfoEFba2RNLJu1
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){

		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou","LTAI5t7z6mkfF5UFuPMcF9EZ","YamClltNMdX0cknBwfoEFba2RNLJu1");
		IAcsClient client = new DefaultAcsClient (profile);
		CommonRequest request = new CommonRequest();
		request.setSysMethod (MethodType.POST);

		request.setSysMethod (MethodType.POST) ;
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms") ;
		request.putQueryParameter("PhoneNumbers", "phoneNumbers");
		request.putQueryParameter("SignName","居居头的验证码");
		request.putQueryParameter("TemplateCode","SMS_462405258");
		request.putQueryParameter("TemplateParam","{\"code\":\"1234\"}");

		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println((request.toString()));
			System.out.println("短信发送成功");
		}catch (ClientException e) {
			e.printStackTrace();
		}

	}




}
