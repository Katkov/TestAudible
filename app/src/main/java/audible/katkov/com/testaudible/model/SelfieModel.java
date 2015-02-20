package audible.katkov.com.testaudible.model;

/**
 * Selfie model that contains urls into three types of images
 */
public class SelfieModel implements IBaseModel {

    private String lowResolution;
    private String tumbnail;
    private String standardResolution;

    public SelfieModel(){

    }

    public SelfieModel(String lowResolution, String tumbnail, String standardResolution) {
        this.lowResolution = lowResolution;
        this.tumbnail = tumbnail;
        this.standardResolution = standardResolution;
    }

    public String getLowResolution() {
        return lowResolution;
    }

    public void setLowResolution(String lowResolution) {
        this.lowResolution = lowResolution;
    }

    public String getTumbnail() {
        return tumbnail;
    }

    public void setTumbnail(String tumbnail) {
        this.tumbnail = tumbnail;
    }

    public String getStandardResolution() {
        return standardResolution;
    }

    public void setStandardResolution(String standardResolution) {
        this.standardResolution = standardResolution;
    }
}
