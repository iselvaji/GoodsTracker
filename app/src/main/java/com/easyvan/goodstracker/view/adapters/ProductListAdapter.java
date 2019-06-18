package com.easyvan.goodstracker.view.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easyvan.goodstracker.R;
import com.easyvan.goodstracker.model.rest.product.pojo.Product;
import com.easyvan.goodstracker.utils.MapUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sm5 on 6/13/2019.
 */

public class ProductListAdapter extends PagedListAdapter<Product, ProductListAdapter.ProductViewHolder> {

    private final Context mContext;
    private final ItemSelectCallback mItemSelectCallback;
    private Location mCurrentlocation;

    public ProductListAdapter(Context context, ItemSelectCallback itemSelectCallback) {
        super(diffCallback);
        mContext = context;
        mItemSelectCallback = itemSelectCallback;
    }

    private static final DiffUtil.ItemCallback<Product> diffCallback = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(Product oldItem, Product newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(Product oldItem, Product newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        final Product product = getItem(position);

        holder.productName.setText(product.getDescription());
        holder.location.setText(product.getLocation().getAddress());

        if (product.getImageUrl() != null) {
            Glide.with(mContext)
                    .load(product.getImageUrl())
                    .error(Glide.with(mContext).load(product.getImageUrl()))
                    .into(holder.productImage);
        }

        if(mCurrentlocation != null) {
            double distance = MapUtils.distanceBetween(mCurrentlocation.getLatitude(), mCurrentlocation.getLongitude(),
                    product.getLocation().getLat(), product.getLocation().getLng(), "K");

            holder.distance.setText(mContext.getString(R.string.txt_unit_km, String.valueOf(distance)));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemSelectCallback.onItemClicked(product, mCurrentlocation);
            }
        });
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image_product) ImageView productImage;
        @BindView(R.id.textview_description) TextView productName;
        @BindView(R.id.textview_location) TextView location;
        @BindView(R.id.textview_distance) TextView distance;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateLocation(Location location) {
        this.mCurrentlocation = location;
        notifyDataSetChanged();
    }
}