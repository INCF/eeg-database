package cz.zcu.kiv.eegdatabase.logic.controller.social;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 29.3.11
 * Time: 14:10
 */
public interface IFBStuffManager {
    public String retrieveAccessToken(String code);
    public String fbLoginAuthenticate();
}
