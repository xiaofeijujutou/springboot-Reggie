// This file is auto-generated, don't edit it. Thanks.
package com.xiaofei.reggie.common;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.aliyun.teaconsole.*;
import com.aliyun.darabonba.env.*;
import com.aliyun.teautil.*;
import com.aliyun.darabonbatime.*;
import com.aliyun.darabonbastring.*;

public class Sample {

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config();
        config.accessKeyId = "LTAI5t7z6mkfF5UFuPMcF9EZ";
        config.accessKeySecret = "YamClltNMdX0cknBwfoEFba2RNLJu1";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static void main(String[] args_) throws Exception {
        Sample.createClient("LTAI5t7z6mkfF5UFuPMcF9EZ", "YamClltNMdX0cknBwfoEFba2RNLJu1");
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dysmsapi20170525.Client client = Sample.createClient(com.aliyun.darabonba.env.EnvClient.getEnv("ACCESS_KEY_ID"), com.aliyun.darabonba.env.EnvClient.getEnv("ACCESS_KEY_SECRET"));
        // 1.发送短信
        SendSmsRequest sendReq = new SendSmsRequest()
                .setPhoneNumbers("17773982935")
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setTemplateParam("{\"code\":\"1234\"}");
        SendSmsResponse sendResp = client.sendSms(sendReq);
        String code = sendResp.body.code;
        if (!com.aliyun.teautil.Common.equalString(code, "OK")) {
            com.aliyun.teaconsole.Client.log("错误信息: " + sendResp.body.message + "");
            return ;
        }

        String bizId = sendResp.body.bizId;
        // 2. 等待 10 秒后查询结果
        com.aliyun.teautil.Common.sleep(10000);
        // 3.查询结果
        java.util.List<String> phoneNums = com.aliyun.darabonbastring.Client.split(args.get(0), ",", -1);
        for (String phoneNum : phoneNums) {
            QuerySendDetailsRequest queryReq = new QuerySendDetailsRequest()
                    .setPhoneNumber(com.aliyun.teautil.Common.assertAsString(phoneNum))
                    .setBizId(bizId)
                    .setSendDate(com.aliyun.darabonbatime.Client.format("yyyyMMdd"))
                    .setPageSize(10L)
                    .setCurrentPage(1L);
            QuerySendDetailsResponse queryResp = client.querySendDetails(queryReq);
            java.util.List<QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO> dtos = queryResp.body.smsSendDetailDTOs.smsSendDetailDTO;
            // 打印结果
            for (QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO dto : dtos) {
                if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "3")) {
                    com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 发送成功，接收时间: " + dto.receiveDate + "");
                } else if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "2")) {
                    com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 发送失败");
                } else {
                    com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 正在发送中...");
                }

            }
        }
    }
}
