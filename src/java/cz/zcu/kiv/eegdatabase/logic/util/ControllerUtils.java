/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.util;

import com.Ostermiller.util.RandPass;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Jindra
 */
public class ControllerUtils {

    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT_WITH_TIME = "dd/MM/yyyy HH:mm";



    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(TIME_FORMAT);
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT);
    }

    public static SimpleDateFormat getDateFormatWithTime() {
        return new SimpleDateFormat(DATE_FORMAT_WITH_TIME);
    }

    /**
     * Helper static function for getting the actual logged user name
     * @return Actual logged user name
     */
    public static String getLoggedUserName() {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o != null) {
            GrantedAuthority[] authorities =
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            for (GrantedAuthority ga : authorities) {
                System.out.println("AUTHORITY: "+ga.getAuthority());
            }
        }

        String userName = null;
        if (o instanceof UserDetails) {
            userName = ((UserDetails) o).getUsername();
        } else if (o instanceof Person) {
            userName = ((Person) o).getUsername();
        } else {
            userName = o.toString();
        }

        return userName;
    }

    /**
     * Helper function for getting MD5 as a string
     *
     * @param sourceString String to count hash from
     * @return MD5 as a string
     * @throws java.security.NoSuchAlgorithmException
     */
  public static String getMD5String(String sourceString) {
    StringBuffer hexString = new StringBuffer();

    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
      md5.update(sourceString.getBytes(), 0, sourceString.length());

      byte[] digest = md5.digest();
      for (int i = 0; i < digest.length; i++) {
        hexString.append(String.format("%02x", digest[i]));
      }

    } catch (NoSuchAlgorithmException ex) {
      // we don't care about the exception - MD5 algorithm is right for sure
    }

    return hexString.toString();
  }

    public static String getRandomPassword() {
        String password = new RandPass().getPass(16);
        return password;
    }
}
