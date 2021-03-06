package com.android.ocasa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.DelegateListAdapter;
import com.android.ocasa.model.Category;
import com.android.ocasa.viewmodel.CategoryViewModel;

/**
 * Created by ignacio on 10/02/16.
 */
public class CategoryAdapter implements DelegateListAdapter{

    static final int CATEGORY_TYPE = 0;

    private CategoryViewModel category;

    public CategoryAdapter(CategoryViewModel category) {
        this.category = category;
    }

    @Override
    public View createView(View convertView, ViewGroup parent, int position) {

        CategoryHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);

            holder = new CategoryHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        }else{
            holder = (CategoryHolder) convertView.getTag();
        }

        holder.name.setText(category.getTitle().toUpperCase());

        return convertView;
    }

    @Override
    public int getViewType() {
        return CATEGORY_TYPE;
    }

    @Override
    public Object getItem() {
        return category;
    }

    public class CategoryHolder{

        TextView name;
    }


}
