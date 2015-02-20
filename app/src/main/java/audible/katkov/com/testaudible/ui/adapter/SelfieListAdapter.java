package audible.katkov.com.testaudible.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import audible.katkov.com.testaudible.R;
import audible.katkov.com.testaudible.TestAudibleApplication;
import audible.katkov.com.testaudible.image_loader.ImageLoader;
import audible.katkov.com.testaudible.model.TreeSelfieModel;
import audible.katkov.com.testaudible.ui.view.ProgressImageView;


public class SelfieListAdapter extends GenericAdapter<TreeSelfieModel> {

    /**
     * This interface allow to catch onClick on images
     */
    public interface ClickImageListener{
        public void onClick(View view);
    }

    private LayoutInflater inflater;
    public ImageLoader imageLoader;
    private ClickImageListener clickImageListener;

    public SelfieListAdapter(Context context, List<TreeSelfieModel> objects, ClickImageListener clickImageListener) {
        super(context, objects);
        inflater = LayoutInflater.from(context);
        imageLoader = ((TestAudibleApplication)context.getApplicationContext()).getImageLoader();
        this.clickImageListener = clickImageListener;
    }

    @Override
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if(convertView == null){
            vi = inflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.imageLarge = (ProgressImageView) vi.findViewById(R.id.selfieLarge);
            holder.imageSmallTop = (ProgressImageView)vi.findViewById(R.id.selfieSmallTop);
            holder.imageSmallBottom = (ProgressImageView)vi.findViewById(R.id.selfieSmallBottom);
            vi.setTag( holder );
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }

        //Set data in a ViewHlder from the current model
        TreeSelfieModel treeSelfieModel = getItem(position);
        imageLoader.DisplayImage(treeSelfieModel.getRoot().getLowResolution(), holder.imageLarge, true);
        imageLoader.DisplayImage(treeSelfieModel.getChildTop().getTumbnail(), holder.imageSmallTop, true);
        imageLoader.DisplayImage(treeSelfieModel.getChildBottom().getTumbnail(), holder.imageSmallBottom, true);
        holder.imageLarge.setTag(treeSelfieModel.getRoot());
        holder.imageSmallBottom.setTag(treeSelfieModel.getChildBottom());
        holder.imageSmallTop.setTag(treeSelfieModel.getChildTop());
        holder.imageLarge.setOnClickListener(onClickListener);
        holder.imageSmallBottom.setOnClickListener(onClickListener);
        holder.imageSmallTop.setOnClickListener(onClickListener);
        return vi;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickImageListener.onClick(v);
        }
    };

    public static class ViewHolder{

        public ProgressImageView imageLarge;
        public ProgressImageView imageSmallTop;
        public ProgressImageView imageSmallBottom;

    }
}
