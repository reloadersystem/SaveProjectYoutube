package ar.reloadersystem.saveproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {

    private Context context;
    //Activity activity;
    ArrayList<VideoDetails> videoDetailsArrayList;


    public MyCustomAdapter(Context context, ArrayList<VideoDetails> videoDetailsArrayList) {
        this.context = context;
        this.videoDetailsArrayList = videoDetailsArrayList;
    }


    @Override
    public Object getItem(int position) {
        return videoDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_list, null);

        }

        ImageView imgfoto = convertView.findViewById(R.id.thumbnailView);
        final TextView title = convertView.findViewById(R.id.thumbnailTitle);
        final TextView descripcion = convertView.findViewById(R.id.textDescription);
        final TextView textURL = convertView.findViewById(R.id.textURL);
        final TextView textVideoID = convertView.findViewById(R.id.textVideoID);



        Glide.with(context)
                .load(videoDetailsArrayList.get(position).getUrl())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgfoto);


        title.setText(videoDetailsArrayList.get(position).getTitle());
       // textVideoID.setText(videoDetailsArrayList.get(position).getVideoId());
        descripcion.setText(videoDetailsArrayList.get(position).getDescription());
       // textURL.setText(videoDetailsArrayList.get(position).getUrl());
        //desahabilitare  estos  2 textos para no mostrar  la  url o el ID y lo pondre  en gone en el xml




        return convertView;
    }

    @Override
    public int getCount() {
        return videoDetailsArrayList.size();
    }


}