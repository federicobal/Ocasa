package com.android.ocasa.receipt.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.ocasa.R;
import com.android.ocasa.receipt.header.EditHeaderReceiptActivity;
import com.android.ocasa.adapter.ReceiptAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.receipt.base.BaseReceiptActivity;
import com.android.ocasa.receipt.detail.DetailReceiptActivity;
import com.android.ocasa.receipt.edit.EditReceiptActivity;
import com.android.ocasa.viewmodel.ReceiptCellViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.codika.androidmvp.fragment.BaseMvpFragment;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptListFragment extends BaseMvpFragment<ReceiptListView, ReceiptListPresenter> implements ReceiptListView{

    static final String ARG_ACTION_ID = "action_id";

    private ListView receiptList;
    private FloatingActionButton addButton;

    public static ReceiptListFragment newInstance(String actionId) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        ReceiptListFragment fragment = new ReceiptListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    private void initControls(View view){
        receiptList = (ListView) view.findViewById(android.R.id.list);

        addButton = (FloatingActionButton) view.findViewById(R.id.add);
    }

    private void setListeners(){

        receiptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ReceiptCellViewModel receiptViewModel = (ReceiptCellViewModel) receiptList.getItemAtPosition(i);

                Intent intent;

                if(receiptViewModel.isOpen()){
                    intent = new Intent(getActivity(), EditReceiptActivity.class);
                }else{
                    intent = new Intent(getActivity(), DetailReceiptActivity.class);
                }

                intent.putExtra(BaseReceiptActivity.EXTRA_RECEIPT_ID, l);
                ((BaseActivity) getActivity()).startNewActivity(intent);

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditHeaderReceiptActivity.class);
                intent.putExtra(EditHeaderReceiptActivity.EXTRA_ACTION_ID, getArguments().getString(ARG_ACTION_ID));
                ((BaseActivity) getActivity()).startNewActivity(intent);
            }
        });
    }

    public void setTitle(String title){
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public ReceiptListView getMvpView() {
        return this;
    }

    @Override
    public Loader<ReceiptListPresenter> getPresenterLoader() {
        return new ReceiptListLoader(getActivity());
    }

    @Override
    public void onReceiptsLoadSuccess(ReceiptTableViewModel table) {
        if (table == null)
            return;

        setTitle("Listado " + table.getName());

        receiptList.setAdapter(new ReceiptAdapter(table.getReceipts()));
    }
}