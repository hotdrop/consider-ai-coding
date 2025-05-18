package jp.hotdrop.considercline.android.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.databinding.ActivityStartBinding

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStartBinding.inflate(layoutInflater) }
    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        // TODO メールアドレスの入力
        // TODO ニックネームの入力
        // TODO 登録ボタンのリスナー
    }

    private fun observe() {
        lifecycle.addObserver(viewModel)
    }

    companion object {
        fun startActivity(activity: AppCompatActivity) = activity.startActivity(Intent(activity, StartActivity::class.java))
    }
}