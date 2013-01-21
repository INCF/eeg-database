package cz.zcu.kiv.eegdatabase.wui.components.utils;

public class StringUtils {
    
    public static String REGEX_ONLY_LETTERS = "[a-zA-Z][a-zA-Z\\s]*";

    private static int randomInt(int min, int max)
    {
        return (int)(Math.random() * (max - min) + min);
    }

    public static String randomString(int min, int max)
    {
        int num = randomInt(min, max);
        byte b[] = new byte[num];
        for (int i = 0; i < num; i++)
            b[i] = (byte)randomInt('a', 'z');
        return new String(b);
    }
    
    public static String getCaptchaString(){
        return randomString(3, 8);
    }
}
