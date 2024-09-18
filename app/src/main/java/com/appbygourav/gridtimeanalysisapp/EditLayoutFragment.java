package com.appbygourav.gridtimeanalysisapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;



import org.jetbrains.annotations.NotNull;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditLayoutFragment extends AppCompatDialogFragment {
    EditText line_width,cell_size;
    int defaultColor= Color.BLACK;
   // Button grid_color_btn;
    DrawView gridDrawView;
    GridLabelView  gridLabelView;
    CardView color_boxf;
    Context context;

    public EditLayoutFragment(DrawView gridDrawView,GridLabelView gridLabelView,Context c1) {
        this.gridDrawView = gridDrawView;
        this.gridLabelView = gridLabelView;
        this.context=c1;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.edit_layout_fragment,null);

        color_boxf=view.findViewById(R.id.fcolor_box);
        line_width=view.findViewById(R.id.fline_width);
        cell_size=view.findViewById(R.id.fcell_size);


        String lineWidthStr=Integer.toString(gridDrawView.getLine_width());
        line_width.setText(lineWidthStr);
        String cellSizeString=Integer.toString(gridDrawView.getCell_size());
        cell_size.setText(cellSizeString);
        color_boxf.setCardBackgroundColor(gridDrawView.getColor());

        line_width.addTextChangedListener(new GenericTextWatcher(gridDrawView,gridLabelView,0));
        cell_size.addTextChangedListener(new GenericTextWatcher(gridDrawView,gridLabelView,1));

        color_boxf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorSelectionWidget();
            }
        });

        builder.setView(view);
        return builder.create();
    }
    public void openColorSelectionWidget() {
        AmbilWarnaDialog colorPickerWidget=new AmbilWarnaDialog(context, Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                    gridDrawView.setColor(color);
                    gridLabelView.setColor(color);
                    gridDrawView.drawAgain();
                    gridLabelView.drawAgain();
                    color_boxf.setCardBackgroundColor(color);
            }
        });
        colorPickerWidget.show();
    }
}
