package jp.hotdrop.considercline.android.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.databinding.ActivityMainBinding
import jp.hotdrop.considercline.android.ui.home.HomeActivity
import jp.hotdrop.considercline.android.ui.start.StartActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

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
    }

    private fun observe() {
        viewModel.userLiveData.observe(this) {
            if (it.isInitialized()) {
                binding.userId.text = getString(R.string.splash_user_id_label, it.userId)
                binding.userId.isVisible = true
                navigationToHome()
            } else {
                binding.progressBar.isVisible = false
                binding.firstStartGroup.isVisible = true
                binding.startButton.setOnClickListener {
                    StartActivity.startActivity(this)
                }
            }
        }
        lifecycle.addObserver(viewModel)
    }

    private fun navigationToHome() {
        HomeActivity.start(this)
        finish()
    }
}
