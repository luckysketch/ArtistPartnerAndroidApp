package com.appbygourav.gridtimeanalysisapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

import org.jetbrains.annotations.NotNull;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditLayoutFragment extends AppCompatDialogFragment {
    EditText line_width,cell_size;
    int mdefaultcolor;
   // Button grid_color_btn;
    DrawView d1;
    CardView color_boxf;
    Context context;

    public EditLayoutFragment(DrawView d1,Context c1) {
        this.d1 = d1;
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



        String str=Integer.toString(d1.line_width);
        line_width.setText(str);
        String str1=Integer.toString(d1.cell_size);
        cell_size.setText(str1);
        color_boxf.setCardBackgroundColor(d1.color);

        line_width.addTextChangedListener(new GenericTextWatcher(d1,0));
        cell_size.addTextChangedListener(new GenericTextWatcher(d1,1));

        color_boxf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencolorf();
            }
        });


        builder.setView(view);

        return builder.create();
    }
    public void opencolorf() {
        AmbilWarnaDialog colorpicker=new AmbilWarnaDialog(context, mdefaultcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mdefaultcolor=color;
                d1.color=mdefaultcolor;
                d1.drawAgain();
                color_boxf.setCardBackgroundColor(mdefaultcolor);
            }
        });
        colorpicker.show();
    }
}
