package com.jlstudio.market.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlstudio.R;

/**
 * Created by gzw on 2016/3/27.
 */
public class DeleteDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView delete;
    private int position;
    private DeleteListener listener;
    public DeleteDialog(Context context,int position,DeleteListener listener) {
        super(context);
        this.context = context;
        this.position = position;
        this.listener = listener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog);
        initView();
    }

    private void initView() {
        delete = (TextView) findViewById(R.id.deletePic);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) delete.getLayoutParams();
        params.width = width;
        delete.setLayoutParams(params);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.delete(position);
        dismiss();
    }
    public interface DeleteListener{
        void delete(int position);
    }
}
