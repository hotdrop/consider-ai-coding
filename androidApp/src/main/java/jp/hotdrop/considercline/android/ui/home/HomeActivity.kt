package jp.hotdrop.considercline.android.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.databinding.ActivityHomeBinding
import jp.hotdrop.considercline.android.ui.pointget.PointGetActivity
import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.model.History
import androidx.recyclerview.widget.RecyclerView
import jp.hotdrop.considercline.android.databinding.HistoryRowBinding
import jp.hotdrop.considercline.model.Point
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels()

    private val pointRefreshLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            onRefreshData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observe()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        binding.currentDateLabel.text = now.format(formatter)

        binding.pointGetButton.setOnClickListener {
            PointGetActivity.startForResult(this, pointRefreshLauncher)
        }

        binding.pointUseButton.setOnClickListener {
            // TODO Jetpack Compose を使ってPointUseScreenを実装する
        }
    }

    private fun observe() {
        viewModel.uiStateLiveData.observe(this) { uiState ->
            viewUserInfo(uiState.appSetting)
            uiState.currentPoint?.let { viewCurrentPoint(it) }
            uiState.histories?.let { viewHistories(it) }
        }
        lifecycle.addObserver(viewModel)
    }

    private fun viewUserInfo(appSetting: AppSetting) {
        if (!appSetting.nickName.isNullOrEmpty()) {
            binding.nickname.text = appSetting.nickName
        }
        if (!appSetting.email.isNullOrEmpty()) {
            binding.email.text = appSetting.email
        }
        binding.homeCardProgressBar.isVisible = false
        binding.homeContentsGroup.isVisible = true
    }

    private fun viewCurrentPoint(point: Point) {
        val pointLabel = getString(R.string.home_point_value, point.balance)
        binding.pointValue.text = pointLabel
    }

    private fun viewHistories(histories: List<History>) {
        if (histories.isEmpty()) {
            return
        }

        binding.historyRecyclerView.adapter = HistoryAdapter(this, histories)
        binding.historyTopBorder.isVisible = true
        binding.historyRecyclerView.isVisible = true
    }

    private fun onRefreshData() {
        viewModel.onLoadCurrentPoint()
        viewModel.onLoadHistory()
    }

    /**
     * ポイント履歴を表示するためのアダプター
     * @property histories 表示する履歴リスト
     */
    class HistoryAdapter(
        private val context: Context,
        private val histories: List<History>
    ) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
        class HistoryViewHolder(val binding: HistoryRowBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
            val binding = HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HistoryViewHolder(binding)
        }

        override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
            val history = histories[position]
            holder.binding.dateLabel.text = history.toStringDateTime()
            holder.binding.historyLabel.text = history.detail
            holder.binding.historyPoint.text = context.getString(R.string.point_history_point_label, history.point)
        }

        override fun getItemCount(): Int = histories.size
    }

    companion object {
        fun start(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, HomeActivity::class.java))
    }
}
