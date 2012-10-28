package cz.zcu.kiv.eegdatabase.logic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.octo.captcha.service.image.ImageCaptchaService;

/**
*
* @author 	 anil_verma
*
*/
@SuppressWarnings("restriction")
public class CaptchaImageGenerator implements Controller, InitializingBean{
	private ImageCaptchaService captchaService;
        public static final String CAPTCHA_ID_RESPONSE_PARAMETER = "captchaId";

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        String captchaId = request.getParameter(CAPTCHA_ID_RESPONSE_PARAMETER);
    	BufferedImage challenge =
                captchaService.getImageChallengeForID(captchaId,request.getLocale());

        ImageIO.write(challenge, "jpeg", jpegOutputStream);
//        JPEGImageEncoder jpegEncoder =
//                JPEGCodec.createJPEGEncoder(jpegOutputStream);
//        jpegEncoder.encode(challenge);
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
        	response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
        return null;
	}


	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}


	public void afterPropertiesSet() throws Exception {
		if(captchaService == null){
			throw new RuntimeException("Image captcha service wasn`t set!");
		}
	}
}
