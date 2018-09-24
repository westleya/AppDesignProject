package com.example.lifestyleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RCViewAdapter extends RecyclerView.Adapter<RCViewAdapter.ViewHolder>{

    // Member variables
    private Context mContext;
    private DataPasser mDataPasser;
    private ArrayList<String> mMenuItem, mMenuDescriptions;

    /**
     * Constructor
     * @param mMenuTitle - List of menu items
     * @param menuDescriptions - Description of menu items
     */
    public RCViewAdapter(ArrayList<String> mMenuTitle, ArrayList<String> menuDescriptions) {
        this.mMenuItem = mMenuTitle;
        this.mMenuDescriptions = menuDescriptions;
    }


    @NonNull
    @Override
    public RCViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        // Associate the callback with this Fragment
        try{
            mDataPasser = (DataPasser) mContext;
        }
        catch(ClassCastException e){
            throw new ClassCastException(mContext.toString()+ " must implement OnDataPass");
        }

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View myView = layoutInflater.inflate(R.layout.menu, parent,false);
        return new ViewHolder(myView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View itemLayout;
        protected TextView itemTvMenu, itemTvDes;

        public ViewHolder(View view){
            super(view);
            itemLayout = view;
            itemTvMenu = (TextView) view.findViewById(R.id.tv_menu);
            itemTvDes = (TextView) view.findViewById(R.id.tv_menu_description);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemTvMenu.setText(mMenuItem.get(position));

        // Controls what happens when an item in the list has been clicked
        holder.itemTvDes.setText(mMenuDescriptions.get(position));
        holder.itemLayout.setOnClickListener(new View.OnClickListener(){
                                                 @Override
                                                 public void onClick(View view) {
                                                     mDataPasser.passData(position);
                                                 }
                                             }
        );
    }


    /**
     * Gets the number of items in the menu
     */
    @Override
    public int getItemCount() {
        return mMenuItem.size();
    }

    /**
     * Interface for the MainActivity
     */
    public static interface DataPasser{
        public void passData(int position);
    }
}
