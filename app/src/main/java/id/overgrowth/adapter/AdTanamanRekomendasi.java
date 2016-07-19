package id.overgrowth.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.overgrowth.R;
import id.overgrowth.model.MTanaman;
import id.overgrowth.utility.UrlApi;

/**
 * Created by bayu_ on 5/23/2016.
 */
public class AdTanamanRekomendasi extends RecyclerView.Adapter<AdTanamanRekomendasi.ViewHolder> {

    private ArrayList<MTanaman> tanaman;
    private Context context;
    public AdTanamanRekomendasi(ArrayList<MTanaman> tanaman, Context context) {
        this.tanaman = tanaman;
        this.context = context;
    }

    @Override
    public AdTanamanRekomendasi.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_list_mulai_tanam_rekomendasi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdTanamanRekomendasi.ViewHolder holder, int position) {
        MTanaman mTanaman = tanaman.get(position);
        holder.namatanaman.setText(mTanaman.getNamatanaman());
        Context context = ((ViewHolder) holder).imagetanaman.getContext();
        Picasso.with(context).load(UrlApi.urlGambarTanaman+ mTanaman.getFototanaman()).into(((ViewHolder) holder).imagetanaman);

    }

    @Override
    public int getItemCount() {
        return tanaman.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namatanaman;
        ImageView imagetanaman;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imagetanaman = (ImageView) itemView.findViewById(R.id.image_foto_tanaman_rekomendasi);
            namatanaman = (TextView) itemView.findViewById(R.id.nama_tanaman_rekomendasi);
        }
    }
}
