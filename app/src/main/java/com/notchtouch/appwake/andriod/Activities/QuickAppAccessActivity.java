package com.notchtouch.appwake.andriod.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notchtouch.appwake.andriod.Models.AppsModel;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityQuickAppAccessBinding;

import java.util.ArrayList;

public class QuickAppAccessActivity extends AppCompatActivity {

    ActivityQuickAppAccessBinding binding;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityQuickAppAccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.quickAccessBackButton.setOnClickListener(v-> onBackPressed());
        String title= getIntent().getStringExtra("quickAccessTitle");
        title= title==null || title.isEmpty() ? getString(R.string.app_name) : title;
        binding.quickAccessTitle.setText(title);

        binding.quickAccessAppsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppRecyclerViewAdapter adapter= new AppRecyclerViewAdapter(QuickAppAccessActivity.this);
        binding.quickAccessAppsRecyclerView.setAdapter(adapter);
        binding.quickAccessAppsRecyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    public static class AppRecyclerViewAdapter extends RecyclerView.Adapter<AppRecyclerViewAdapter.ListViewHolder>{

        Activity activity;
        ArrayList<AppsModel> arrayList;

        public AppRecyclerViewAdapter(Activity activity) {
            this.activity = activity;
            arrayList= Functions.getAppsData(activity);
        }

        @NonNull
        @Override
        public AppRecyclerViewAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, null);
            return new ListViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull AppRecyclerViewAdapter.ListViewHolder holder, int position) {
            setUpdateAppItemUI(holder, position);
            boolean app_selected= isCurrentAppSelected(position);
            holder.appItemName.setOnClickListener(v -> {
                if(!app_selected){
                    Functions.putSharedPref(activity, Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_SELECTED_APP, "string", arrayList.get(position).getPackageName());
                    setUpdateAppItemUI(holder, position);
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();
                }
            });
        }

        private void setUpdateAppItemUI(ListViewHolder holder, int position){
            boolean app_selected= isCurrentAppSelected(position);
            holder.appItemName.setTextColor(ContextCompat.getColor(activity, app_selected ? R.color.white : R.color.black));
            holder.appItemName.setBackgroundTintList(app_selected ? ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.themeColor)) : null);
            holder.appItemName.setText(arrayList.get(position).getAppName());
        }

        private boolean isCurrentAppSelected(int position){
            String selected_app= Functions.getSharedPref(activity, Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_SELECTED_APP, "string", activity.getPackageName());
            return selected_app != null && selected_app.equals(arrayList.get(position).getPackageName());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            TextView appItemName;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                appItemName= itemView.findViewById(R.id.appItemName);
            }
        }
    }
}