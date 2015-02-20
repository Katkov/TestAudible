package audible.katkov.com.testaudible.model;

/**
 * Wrapper of three SelfieModel
 */
public class TreeSelfieModel implements IBaseModel{

    private SelfieModel root;
    private SelfieModel childTop;
    private SelfieModel childBottom;

    public TreeSelfieModel(){

    }

    public TreeSelfieModel(SelfieModel root, SelfieModel childTop, SelfieModel childBottom) {
        this.root = root;
        this.childTop = childTop;
        this.childBottom = childBottom;
    }

    public SelfieModel getRoot() {
        return root;
    }

    public void setRoot(SelfieModel root) {
        this.root = root;
    }

    public SelfieModel getChildTop() {
        return childTop;
    }

    public void setChildTop(SelfieModel childTop) {
        this.childTop = childTop;
    }

    public SelfieModel getChildBottom() {
        return childBottom;
    }

    public void setChildBottom(SelfieModel childBottom) {
        this.childBottom = childBottom;
    }
}
