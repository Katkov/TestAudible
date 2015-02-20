package audible.katkov.com.testaudible.async;


import audible.katkov.com.testaudible.model.SelfieListModel;
import audible.katkov.com.testaudible.parser.SelfieListParser;

public class NetworkManager {

    private String startUrl = "https://api.instagram.com/v1/tags/selfie/media/recent?client_id=586eb6ac6a5b4b63a03ec001ed9e09ee";
    private SelfieListModel selfieModelList;

    public NetworkManager(){
        selfieModelList = new SelfieListModel();
    }

    /**
     * Load selfies from the Internet
     * @param url
     * @param responseCallback
     */
    private void loadSelfies(String url, Callback<ResponseModel<SelfieListModel>> responseCallback){
        RequestModel<ResponseModel<SelfieListModel>> requestModel =
                new RequestModel<>();
        requestModel.url = url;
        requestModel.parser = new SelfieListParser();
        requestModel.callback = responseCallback;
        NetworkParserTask networkParserTask = new NetworkParserTask(requestModel);
        networkParserTask.execute();
    }

    /**
     * Load start selfies
     * @param responseCallback
     */
    public void loadStartSelfies(Callback<ResponseModel<SelfieListModel>> responseCallback){
        loadSelfies(startUrl,responseCallback);
    }

    /**
     * Load next selfies
     * @param responseCallback
     */
    public void loadNextSelfies(Callback<ResponseModel<SelfieListModel>> responseCallback){
        loadSelfies(selfieModelList.getNextUrl(),responseCallback);
    }

    public SelfieListModel getSelfieListModel() {
        return selfieModelList;
    }

    public void addSelfieModelList(SelfieListModel selfieModelList) {
        this.selfieModelList.setNextUrl(selfieModelList.getNextUrl());
        this.selfieModelList.getSelfieModelList().addAll(selfieModelList.getSelfieModelList());
    }

    public void clearSelfie(){
        selfieModelList = new SelfieListModel();
    }
}
