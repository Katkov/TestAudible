package audible.katkov.com.testaudible.parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import audible.katkov.com.testaudible.model.IBaseModel;
import audible.katkov.com.testaudible.model.SelfieListModel;
import audible.katkov.com.testaudible.model.SelfieModel;
import audible.katkov.com.testaudible.model.TreeSelfieModel;

/**
 * Implementation of IBaseParser for current model
 */
public class SelfieListParser implements IBaseParser{


    /**
     * Parse model that used in this application
     * @param string that contain JSON, which should be parsed in model
     * @return
     * @throws JSONException
     */
    @Override
    public IBaseModel parse(String string) throws JSONException {
        SelfieListModel selfieListModel = new SelfieListModel();
        JSONObject mainJsonObject = new JSONObject(string);
        JSONObject peginationObject = mainJsonObject.optJSONObject("pagination");
        selfieListModel.setNextUrl(peginationObject.optString("next_url"));
        JSONArray dataArray = mainJsonObject.optJSONArray("data");
        List<TreeSelfieModel> treeSelfieModelList = new ArrayList<>();
        TreeSelfieModel treeSelfieModel = new TreeSelfieModel();
        for(int i = 0; i < dataArray.length(); i++){
            JSONObject dataObject = dataArray.optJSONObject(i);
            JSONObject imagesObject = dataObject.optJSONObject("images");

            SelfieModel selfieModel = new SelfieModel();

            JSONObject lowResolutionObject = imagesObject.optJSONObject("low_resolution");
            selfieModel.setLowResolution(lowResolutionObject.optString("url"));

            JSONObject thumbnailObject = imagesObject.optJSONObject("thumbnail");
            selfieModel.setTumbnail(thumbnailObject.optString("url"));

            JSONObject standardResolutionObject = imagesObject.optJSONObject("standard_resolution");
            selfieModel.setStandardResolution(standardResolutionObject.optString("url"));

            if(treeSelfieModel.getRoot() == null){
                treeSelfieModel.setRoot(selfieModel);
            }else if(treeSelfieModel.getChildTop() == null){
                treeSelfieModel.setChildTop(selfieModel);
            }else if(treeSelfieModel.getChildBottom() == null){
                treeSelfieModel.setChildBottom(selfieModel);
            }else{
                treeSelfieModelList.add(treeSelfieModel);
                treeSelfieModel = new TreeSelfieModel();
            }
        }
        if(treeSelfieModel.getRoot() != null){
            treeSelfieModelList.add(treeSelfieModel);
        }
        selfieListModel.setSelfieModelList(treeSelfieModelList);
        return selfieListModel;
    }
}
