package audible.katkov.com.testaudible.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import audible.katkov.com.testaudible.R;
import audible.katkov.com.testaudible.TestAudibleApplication;
import audible.katkov.com.testaudible.async.Callback;
import audible.katkov.com.testaudible.async.NetworkManager;
import audible.katkov.com.testaudible.async.ResponseModel;
import audible.katkov.com.testaudible.image_loader.ImageLoader;
import audible.katkov.com.testaudible.model.SelfieListModel;
import audible.katkov.com.testaudible.model.SelfieModel;
import audible.katkov.com.testaudible.ui.adapter.EndlessScrollListener;
import audible.katkov.com.testaudible.ui.adapter.SelfieListAdapter;
import audible.katkov.com.testaudible.ui.view.ProgressImageView;

public class MainActivity extends ActionBarActivity implements Callback<ResponseModel<SelfieListModel>>{

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    private ListView listView;
    private SelfieListAdapter adapter;
    private NetworkManager networkManager;
    private ImageLoader imageLoader;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkManager = ((TestAudibleApplication)getApplication()).getNetworkManager();
        imageLoader = ((TestAudibleApplication)getApplication()).getImageLoader();
        listView = (ListView)findViewById(R.id.list);
        adapter = new SelfieListAdapter(this, networkManager.getSelfieListModel().getSelfieModelList(),
                                        clickImageListener);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                networkManager.loadNextSelfies(MainActivity.this);
            }
        });
        networkManager.loadStartSelfies(this);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    }

    /**
     * zoom image
     */
    private SelfieListAdapter.ClickImageListener clickImageListener = new SelfieListAdapter.ClickImageListener() {
        @Override
        public void onClick(View view) {
            ProgressImageView progressImageView = (ProgressImageView) view;
            zoomImageFromThumb(view, progressImageView);
        }
    };

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        listView.setAdapter(null);
        //networkManager.clearSelfie();
        super.onDestroy();
    }

    @Override
    public void call(ResponseModel<SelfieListModel> response) {
        if (response.model != null) {
            networkManager.addSelfieModelList(response.model);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this,"Network error",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Zoom image and show it with animation on the center of the screen
     * @param thumbView current view
     * @param progressImageView wrapper of current view with information about url throw getTag method
     */
    private void zoomImageFromThumb(final View thumbView, ProgressImageView progressImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ProgressImageView expandedImageView = (ProgressImageView) findViewById(R.id.expanded_image);
        expandedImageView.getImageView().setImageDrawable(progressImageView.getImageView().getDrawable());
        imageLoader.DisplayImage(((SelfieModel)progressImageView.getTag()).getStandardResolution(),
                                   expandedImageView, false);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
