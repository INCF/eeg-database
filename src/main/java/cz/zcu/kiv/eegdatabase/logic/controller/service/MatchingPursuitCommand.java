package cz.zcu.kiv.eegdatabase.logic.controller.service;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 28.4.2011
 * Time: 20:29:28
 * To change this template use File | Settings | File Templates.
 */
public class MatchingPursuitCommand {

    private int channel;
    private int atom;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getAtom() {
        return atom;
    }

    public void setAtom(int atom) {
        this.atom = atom;
    }
}
