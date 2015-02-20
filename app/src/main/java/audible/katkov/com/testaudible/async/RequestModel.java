package audible.katkov.com.testaudible.async;


import audible.katkov.com.testaudible.parser.IBaseParser;

public class RequestModel<T> {

    public String url;
    public IBaseParser parser;
    public Callback<T> callback;

}
