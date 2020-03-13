package com.example.RESTful.validate.impl;

import com.example.RESTful.validate.code.ValidateCode;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode>{
    @Override
    protected ValidateCode generate(ServletWebRequest request) {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0;i<getSikieduSecurityProperties().getCodeProperties().getSmsCodeProperties().getLength();i++){
            code.append(r.nextInt(10));
        }
        return new ValidateCode(code.toString(),getSikieduSecurityProperties().getCodeProperties().getSmsCodeProperties().getExpireIn());
    }

    @Override
    protected void send(ServletWebRequest request,HttpServletResponse response, ValidateCode validateCode) throws IOException {
        int app_id = 1400322825;
        String app_key = "e21ff9359b9d43054587ebae78e89080";
        int template_id = 542848;
        String sign = "Temunper公众号";
        String[] params = new String[1];
        params[0] = validateCode.getCode();
        System.out.println(params[0]);
        SmsSingleSender sSender = new SmsSingleSender(app_id,app_key);
        try {
            SmsSingleSenderResult result = sSender.sendWithParam("86", ServletRequestUtils.getRequiredStringParameter(request.getRequest(),"mobile"),template_id,params,sign,"","");
            System.out.println(result);

        } catch (IOException | HTTPException | ServletRequestBindingException e) {
            e.printStackTrace();
        }
    }



//
//    @Override
//    protected void send() {
//        int app_id = 1400322825;
//        String app_key = "e21ff9359b9d43054587ebae78e89080";
//        int template_id = 542848;
//        String sign = "Temunper公众号";
//        String[] params = new String[1];
//        params[0] = code;
//        System.out.println(code);
//        SmsSingleSender sSender = new SmsSingleSender(app_id,app_key);
//        try {
//            SmsSingleSenderResult result = sSender.sendWithParam("86",mobile,template_id,params,sign,"","");
//            System.out.println(result);
//
//        } catch (IOException | HTTPException e) {
//            e.printStackTrace();
//        }
//    }
}
