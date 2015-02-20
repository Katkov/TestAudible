package audible.katkov.com.testaudible.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model that contains list of selfies
 */
public class SelfieListModel implements IBaseModel{
    
    private String nextUrl;
    private List<TreeSelfieModel> selfieModelList;

    public SelfieListModel() {
        selfieModelList = new ArrayList<>();
    }

    public SelfieListModel(String nextUrl, List<TreeSelfieModel> selfieModelList) {
        this.nextUrl = nextUrl;
        this.selfieModelList = selfieModelList;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public List<TreeSelfieModel> getSelfieModelList() {
        return selfieModelList;
    }

    public void setSelfieModelList(List<TreeSelfieModel> selfieModelList) {
        this.selfieModelList = selfieModelList;
    }
}
