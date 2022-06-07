package tdtu.finalproject.salesmanager.History;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tdtu.finalproject.salesmanager.Cart.Cart_Items_Adapter;
import tdtu.finalproject.salesmanager.R;

public class History_Items_Adapter extends RecyclerView.Adapter<History_Items_Adapter.HistoryViewHolder> {
    private List<JSONObject> listHistory;
    private ConstraintLayout infoLayout;

    public History_Items_Adapter() {}

    public History_Items_Adapter(List<JSONObject> listHistory,ConstraintLayout infoLayout){
        this.listHistory = listHistory;
        this.infoLayout = infoLayout;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_card,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        JSONObject hist_items = this.listHistory.get(position);
        String tmpDate = "";
        String tmpTotal = "";

        try {
            tmpTotal = hist_items.getString("total");
            holder.historyTotal.setText("Pay: "+tmpTotal+" VNĐ");
            tmpDate = hist_items.getString("pay_date");
            holder.historyDate.setText("Date: "+tmpDate);
            holder.infoArray = hist_items.getJSONArray("itemlist");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<String> tmpListInfo  = new ArrayList<>();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Constraint Layout
                infoLayout.setVisibility(View.VISIBLE);

                RecyclerView tmpRecycler = infoLayout.findViewById(R.id.history_bill_info_list);
                ImageButton tmpExit = infoLayout.findViewById(R.id.exit_bill_info);
                TextView tmpTotal = infoLayout.findViewById(R.id.total_bill_info);

                // Get Bill Info
                try {
                    for(int i = 0; i<holder.infoArray.length() ; i++){
                        JSONObject tmpObject = holder.infoArray.getJSONObject(i);

                        String tmpString = tmpObject.getString("item_name") + "+"
                                                + tmpObject.getString("priceperone") + "+"
                                                    + tmpObject.getString("quantity");
                        tmpListInfo.add(tmpString);
                    }

                    tmpTotal.setText("Total: "+hist_items.getString("total")+ " VNĐ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Set Bill to RecyclerView
                tmpRecycler.setHasFixedSize(true);
                tmpRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 1, GridLayoutManager.VERTICAL, false));
                Cart_Items_Adapter  tmpadapter = new Cart_Items_Adapter(tmpListInfo);
                tmpRecycler.setAdapter(tmpadapter);

                // Exit Info
                tmpExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        infoLayout.setVisibility(View.INVISIBLE);
                        tmpListInfo.clear();
                        tmpadapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView historyTotal,historyDate;
        JSONArray infoArray = new JSONArray();

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            historyTotal = itemView.findViewById(R.id.history_pay);
            historyDate = itemView.findViewById(R.id.histoday_date);
        }

    }
}

