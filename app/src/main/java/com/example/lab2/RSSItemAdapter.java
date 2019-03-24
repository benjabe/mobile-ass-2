package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

public class RSSItemAdapter
        extends RecyclerView.Adapter<RSSItemAdapter.RSSItemViewHolder> {
    private Context mContext;
    private List<RSSItem> mItemList;

    //private OnRecyclerViewItemClickListener mListener;


    RSSItemAdapter(Context mContext, List<RSSItem> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }

    /*
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

*/
    @NonNull
    @Override
    public RSSItemViewHolder
    onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_items, null);
        return new RSSItemViewHolder(view);
    }

    @Override
    public void
    onBindViewHolder(@NonNull final RSSItemViewHolder rssItemViewHolder, int i) {
        RSSItem item = mItemList.get(i);

        /*
        rssItemViewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerViewItemClicked(
                        rssItemViewHolder.getAdapterPosition(),
                        -1
                );
            }
        });
        */

        rssItemViewHolder.txtTitle.setText(item.getTitle());
        rssItemViewHolder.txtPubDate.setText(item.getPubDate());
        rssItemViewHolder.txtDesc.setHtml(
                item.getDescription(),
                new HtmlHttpImageGetter(rssItemViewHolder.txtDesc)
        );
        rssItemViewHolder.txtUrl.setText(item.getLink());

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class RSSItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtPubDate;
        HtmlTextView txtDesc;
        TextView txtUrl;

        LinearLayout parentView;

        RSSItemViewHolder(@NonNull View itemView) {
            super(itemView);
            final Context context = itemView.getContext();

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtPubDate = itemView.findViewById(R.id.txt_pub_date);
            txtDesc = itemView.findViewById(R.id.txt_description);
            txtUrl = itemView.findViewById(R.id.txt_url);

            parentView = itemView.findViewById(R.id.item_parent_view);
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(context, ItemActivity.class);
                    //intent.putExtra("url", txtUrl.getText());
                    //context.startActivity(intent);

                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(txtUrl.getText().toString())
                    );
                    context.startActivity(intent);
                }
            });
        }
    }
}
