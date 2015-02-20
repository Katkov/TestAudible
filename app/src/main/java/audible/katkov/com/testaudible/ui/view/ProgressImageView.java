package audible.katkov.com.testaudible.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import audible.katkov.com.testaudible.R;

/**
 * Created by eloor_000 on 2/20/2015.
 */
public class ProgressImageView extends RelativeLayout{

    private ImageView imageView;
    private ProgressBar progressBar;

    public ProgressImageView(Context context) {
        super(context);
        initLayout();
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout(){
        inflate(getContext(), R.layout.image_view_progress, this);
        imageView = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        setProgressBarVisibility(INVISIBLE);
    }

    public void setImageVisible(int visibiity){
        imageView.setVisibility(visibiity);
    }

    public void setProgressBarVisibility(int visibility){
        progressBar.setVisibility(visibility);
    }

    public ImageView getImageView(){
        return imageView;
    }

    /**
     * Make animation on imageview
     */
    public void fadeInImage(){
        imageView.setVisibility(VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        imageView.setAnimation(fadeIn);
    }

}
