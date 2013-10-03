/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   CaptchaImageGenerator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
