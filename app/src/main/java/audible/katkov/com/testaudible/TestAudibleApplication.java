package audible.katkov.com.testaudible;

import android.app.Application;

import audible.katkov.com.testaudible.async.NetworkManager;
import audible.katkov.com.testaudible.image_loader.ImageLoader;

public class TestAudibleApplication extends Application{

    private NetworkManager networkManager;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        networkManager = new NetworkManager();
        imageLoader = new ImageLoader(this);
    }

    public NetworkManager getNetworkManager(){
        if(networkManager == null){
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    public ImageLoader getImageLoader(){
        if(imageLoader == null){
            imageLoader = new ImageLoader(this);
        }
        return imageLoader;
    }
}
