package cz.zcu.kiv.eegdatabase.logic.controller.service;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 17.9.11
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public enum ServicesList {
    MATCHING_PURSUIT("Matching Pursuit"),
//    DISCRETE_WAVELET("Discrete Wavelet Transformation"),
//    CONTINUOUS_WAVELET("Continuous Wavelet Transformation"),
    FAST_FOURIER("Fast Fourier");

    private final String name;

    ServicesList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
