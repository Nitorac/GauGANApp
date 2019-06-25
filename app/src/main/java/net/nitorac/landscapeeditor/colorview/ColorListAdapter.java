package net.nitorac.landscapeeditor.colorview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import net.nitorac.landscapeeditor.InputFragment;
import net.nitorac.landscapeeditor.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Nitorac.
 */
public class ColorListAdapter extends ArrayAdapter<Map.Entry<String, Integer>> {

    private InputFragment frag;
    private List<Map.Entry<String, Integer>> items;

    public ColorListAdapter(@NonNull InputFragment frag, List<Map.Entry<String, Integer>> mItems) {
        super(frag.getActivity(), 0, mItems);
        items = mItems;
        this.frag = frag;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Map.Entry<String, Integer> getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(frag.getActivity()).inflate(R.layout.color_item, parent, false);
        ((ColorView)convertView.findViewById(R.id.colorViewItem)).setColorIndex(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.updateColor(((ColorView)v.findViewById(R.id.colorViewItem)).getColor());
                if(frag.getColorDialog() != null && frag.getColorDialog().isShowing()){
                    frag.getColorDialog().dismiss();
                }
            }
        });
        return convertView;
    }
}
