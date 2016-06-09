package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Status;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.PairViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 02/06/16.
 */
public class DetailReceiptStatusTaskLoader  extends AsyncTaskLoader<FormViewModel> {

    private FormViewModel status;

    private long receiptId;

    public DetailReceiptStatusTaskLoader(Context context, long receiptId) {
        super(context);
        this.receiptId = receiptId;
    }

    @Override
    public FormViewModel loadInBackground() {

        FormViewModel form = new FormViewModel();

        Receipt receipt = new ReceiptDAO(getContext()).findById(receiptId);

        Status status = receipt.getStatus();

        FieldViewModel latitude = new FieldViewModel();
        latitude.setTag("Latitud");
        latitude.setLabel("Latitud");
        latitude.setValue(String.valueOf(status.getLatitude()));
        latitude.setType(FieldType.TEXT);

        FieldViewModel longitude = new FieldViewModel();
        longitude.setTag("Longitud");
        longitude.setLabel("Longitud");
        longitude.setValue(String.valueOf(status.getLongitude()));
        longitude.setType(FieldType.TEXT);

        PairViewModel location = new PairViewModel();
        location.setFirstField(latitude);
        location.setSecondField(longitude);

        form.addPair(location);

        FieldViewModel systemDate = new FieldViewModel();
        systemDate.setTag("Fecha sistema");
        systemDate.setLabel("Fecha sistema");
        systemDate.setValue(status.getSystemDate());
        systemDate.setType(FieldType.DATETIME);

        FieldViewModel timezone = new FieldViewModel();
        timezone.setTag("Zona Horaria");
        timezone.setLabel("Zona Horaria");
        timezone.setValue(status.getTimezone());
        timezone.setType(FieldType.TEXT);

        PairViewModel pairViewModel = new PairViewModel();
        pairViewModel.setFirstField(systemDate);
        pairViewModel.setSecondField(timezone);

        form.addPair(pairViewModel);

        Action action = receipt.getAction();

        List<ColumnAction> columnActions = new ColumnActionDAO(getContext()).findColumnsForActionAndType(action.getId(), ColumnAction.ColumnActionType.HEADER);

        form.setTitle(action.getName());

        List<Field> headers = new FieldDAO(getContext()).findForReceipt(receiptId);

        for (Field header : headers){

            FieldViewModel field = new FieldViewModel();
            field.setTag(header.getColumn().getId());
            field.setLabel(header.getColumn().getName());
            field.setValue(header.getValue());
            field.setType(header.getColumn().getFieldType() != FieldType.DATETIME ? FieldType.TEXT : FieldType.DATETIME);

            PairViewModel pair = new PairViewModel();
            pair.setFirstField(field);

            if(header.getColumn().getFieldType() == FieldType.COMBO){

                Column column = header.getColumn();

                List<Column> relationship = new ColumnDAO(getContext()).findLogicColumnsForTable(column.getRelationship().getId());

                List<FieldViewModel> fields = new ArrayList<>();

                Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(column.getRelationship().getId(), header.getValue());

                if(record != null)
                    record.setFields(new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(record.getId())));

                for (Column col : relationship){

//                    FieldViewModel subField = new FieldViewModel();
//                    subField.setLabel(col.getName());
//                    subField.setType(col.getFieldType());
//                    subField.setTag(String.valueOf(col.getId()));

                    if(record != null) {
                        //subField.setValue(record.getFieldForColumn(col.getId()).getValue());
                        field.setValue(record.getFieldForColumn(col.getId()).getValue());
                    }

//                    fields.add(subField);
                }

                field.setRelationshipFields(fields);

                FieldViewModel lastField = loadLastValue(header, columnActions);
                if(lastField != null)
                    pair.setSecondField(lastField);
            }
//
            form.addPair(pair);
        }

        return form;
    }

    private FieldViewModel loadLastValue(Field field, List<ColumnAction> columnActions){

        for (ColumnAction columnAction : columnActions){

            if(columnAction.getColumn().getId().equalsIgnoreCase(field.getColumn().getId()) &&
                    columnAction.getLastValue() != null) {
                FieldViewModel fieldViewModel = new FieldViewModel();
                fieldViewModel.setTag("Ultimo valor");
                fieldViewModel.setLabel("Ultimo valor");
                fieldViewModel.setValue(columnAction.getLastValue());
                fieldViewModel.setType(FieldType.TEXT);

                if (columnAction.getColumn().getFieldType() == FieldType.COMBO) {

                    Column column = columnAction.getColumn();

                    List<Column> relationship = new ColumnDAO(getContext()).findLogicColumnsForTable(column.getRelationship().getId());

                    List<FieldViewModel> fields = new ArrayList<>();

                    Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(column.getRelationship().getId(), columnAction.getLastValue());

                    if (record != null)
                        record.setFields(new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(record.getId())));

                    for (Column col : relationship) {

//                    FieldViewModel subField = new FieldViewModel();
//                    subField.setLabel(col.getName());
//                    subField.setType(col.getFieldType());
//                    subField.setTag(String.valueOf(col.getId()));

                        if (record != null) {
                            //subField.setValue(record.getFieldForColumn(col.getId()).getValue());
                            fieldViewModel.setValue(record.getFieldForColumn(col.getId()).getValue());
                        }

//                    fields.add(subField);
                    }

                    fieldViewModel.setRelationshipFields(fields);
                }

                return  fieldViewModel;
            }
        }

        return null;
    }

    @Override
    public void deliverResult(FormViewModel data) {
        this.status = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(status != null)
            deliverResult(status);
        else{
            forceLoad();
        }
    }
}