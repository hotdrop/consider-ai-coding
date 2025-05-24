package jp.hotdrop.considercline.android.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels()

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

        // TODO Pointカードを構築
    }

    private fun observe() {
        lifecycle.addObserver(viewModel)
    }

    companion object {
        fun start(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, HomeActivity::class.java))
    }
}