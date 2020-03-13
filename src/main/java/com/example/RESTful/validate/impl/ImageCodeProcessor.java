package com.example.RESTful.validate.impl;

import com.example.RESTful.validate.code.ImageCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Component("imageCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode>{
    @Override
    protected ImageCode generate(ServletWebRequest request) {
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", getSikieduSecurityProperties().getCodeProperties().getImageCodeProperties().getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", getSikieduSecurityProperties().getCodeProperties().getImageCodeProperties().getHeight());
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
        for (int i = 0; i < getSikieduSecurityProperties().getCodeProperties().getImageCodeProperties().getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i, 16);
        }

        g.dispose();
        return new ImageCode(image, sRand, getSikieduSecurityProperties().getCodeProperties().getImageCodeProperties().getExpireIn());
    }

    @Override
    protected void send(ServletWebRequest request,HttpServletResponse response, ImageCode imageCode) throws IOException {

        assert imageCode != null;

        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

}
