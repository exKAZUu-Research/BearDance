package net.exkazuu.mimicdance.pages.editor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.program.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ツールボックス用のAdapter
 */
public class ToolboxAdapter extends RecyclerView.Adapter<ToolboxAdapter.ViewHolder> {

    /**
     * コマンドがタップされたのを通知するためのinterface
     */
    public interface OnCommandClickListener {
        /**
         * コマンドがタップされたときに呼ばれます
         *
         * @param command タップされたコマンド
         */
        void onCommandClick(String command);
    }

    private final LayoutInflater mInflater;
    private final OnCommandClickListener mListener;
    private final List<String> mCommandList = new ArrayList<>();
    private static int mSelectedPosition;


    public ToolboxAdapter(Context context, OnCommandClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mListener = listener;
        mSelectedPosition=-1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_toolbox, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String command = mCommandList.get(position);
        holder.mCommand=command;
        holder.mIcon.setImageResource(Command.getImage(command));
//        holder.bind(command,position,mSelectedPosition);

        Context context = holder.mIcon.getContext();
        if (mSelectedPosition == position) {
            holder.mIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.program_selected));
        } else {
            holder.mIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.transparency));
        }

    }

    @Override
    public int getItemCount() {
        return mCommandList.size();
    }


    public void setCommands(String[] commands) {
        mCommandList.clear();
        mCommandList.addAll(Arrays.asList(commands));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.icon)
        ImageView mIcon;
        private final OnCommandClickListener mListener;
        private String mCommand;

        public ViewHolder(View itemView, OnCommandClickListener listener) {
            super(itemView);
            mListener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.icon)
        void iconClicked() {
            if (mListener == null) {
                return;
            }

            mSelectedPosition = getAdapterPosition();

//            mIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.program_selected));
            mListener.onCommandClick(mCommand);


        }
        void bind(String command, int position,int selectedPosition) {
            mIcon.setImageResource(Command.getImage(command));

        }

    }
}
