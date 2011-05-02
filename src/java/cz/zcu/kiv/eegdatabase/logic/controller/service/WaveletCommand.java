package cz.zcu.kiv.eegdatabase.logic.controller.service;

public class WaveletCommand {

    private int channel;
    private String wavelet;

    public int getChannel() {
        return channel;
    }

    public String getWavelet() {
        return wavelet;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setWavelet(String wavelet) {
        this.wavelet = wavelet;
    }
}
