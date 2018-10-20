package trainee_piurko.prospektdev.com;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView itemRecyclerView;
    private List<AppItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemRecyclerView = (RecyclerView) findViewById(R.id.treinee_app_recycler_view);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        new FetchItemTask().execute();

        setudAdapter();
    }

    private void setudAdapter(){
        itemRecyclerView.setAdapter(new ItemAdapter(mItems));
    }

    private class ItemHolder extends RecyclerView.ViewHolder{

        private ImageView itemImageViev;

        public ItemHolder(View itemView) {
            super(itemView);
            itemImageViev = (ImageView) itemView.findViewById(R.id.treinee_app_image_view);

        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder>{
        private List<AppItem> mAppItems;

        public ItemAdapter(List<AppItem> items){
            mAppItems = items;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View v = inflater.inflate(R.layout.app_item, parent,false);
            return new ItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            AppItem appItem = mAppItems.get(position);
            Glide.with(MainActivity.this).load(appItem.getUrl()).into(holder.itemImageViev);
        }

        @Override
        public int getItemCount() {
            return mAppItems.size();
        }
    }

    private class FetchItemTask extends AsyncTask<Void,Void, List<AppItem>>{

        @Override
        protected List<AppItem> doInBackground(Void... voids) {
            return new PinterestExplorer().feachItems();

        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            mItems = appItems;
            setudAdapter();
        }
    }
}
