package jp.hotdrop.considercline.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.hotdrop.considercline.android.databinding.HistoryRowBinding
import jp.hotdrop.considercline.model.History

/**
 * ポイント履歴を表示するためのRecyclerView.Adapter
 * @property histories 表示する履歴リスト
 */
class HistoryAdapter(private val histories: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    /**
     * 各履歴アイテムのビューを保持するViewHolder
     * @property binding HistoryRowBindingのインスタンス
     */
    class HistoryViewHolder(val binding: HistoryRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]
        holder.binding.dateLabel.text = history.toStringDateTime()
        holder.binding.historyLabel.text = history.detail
        holder.binding.historyPoint.text = "${history.point} ポイント"
    }

    override fun getItemCount(): Int = histories.size
}
