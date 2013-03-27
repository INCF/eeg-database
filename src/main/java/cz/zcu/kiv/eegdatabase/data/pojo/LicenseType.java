package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 *
 * @author Jakub Danek
 */
public enum LicenseType {
    /**
     * Public experiments
     */
    OPEN_DOMAIN,
    /**
     * Experiments published for research/education purposes only
     */
    ACADEMIC,
    /**
     * For any purpose, paid
     */
    BUSINESS,
    /**
     * Private experiments
     */
    PRIVATE
}
