package audible.katkov.com.testaudible.parser;



import org.json.JSONException;

import audible.katkov.com.testaudible.model.IBaseModel;

public interface IBaseParser{

    public IBaseModel parse(String string) throws JSONException;

}
