package com.example.RESTful.controller;


import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.validate.code.ImageCode;
import com.example.RESTful.validate.code.ValidateCode;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@RestController
public class ValidateCodeController {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    public static String sessionKey = "session_key_image_code";
    public static String sessionSmsKey = "session_key_sms_code";

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException {
        ValidateCode smsCode = createSmsCode();

        sessionStrategy.setAttribute(new ServletWebRequest(request), sessionSmsKey, smsCode);

        String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");

        sendSms(mobile, smsCode.getCode());
    }


    @GetMapping("/code/image")
    private void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = createImageCode(request);

        sessionStrategy.setAttribute(new ServletWebRequest(request), sessionKey, imageCode);


    }

    private ImageCode createImageCode(HttpServletRequest request) {
        int width = ServletRequestUtils.getIntParameter(request, "width", sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getWidth());
        int height = ServletRequestUtils.getIntParameter(request, "height", sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getHeight());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.setColor(new Color(x, y, x + x1, y + y1));
        }

        String sRand = "";
        for (int i = 0; i < sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i, 16);
        }

        g.dispose();
        return new ImageCode(image, sRand, sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getExpireIn());
    }


    private void sendSms(String mobile, String code) {
        int app_id = 1400322825;
        String app_key = "e21ff9359b9d43054587ebae78e89080";
        int template_id = 542848;
        String sign = "Temunper公众号";
        String[] params = new String[1];
        params[0] = code;
        System.out.println(code);
        SmsSingleSender sSender = new SmsSingleSender(app_id,app_key);
        try {
            SmsSingleSenderResult result = sSender.sendWithParam("86",mobile,template_id,params,sign,"","");
            System.out.println(result);

        } catch (IOException | HTTPException e) {
            e.printStackTrace();
        }
    }

    public ValidateCode createSmsCode() {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0;i<sikieduSecurityProperties.getCodeProperties().getSmsCodeProperties().getLength();i++){
            code.append(r.nextInt(10));
        }
        return new ValidateCode(code.toString(),sikieduSecurityProperties.getCodeProperties().getSmsCodeProperties().getExpireIn());
    }
}
