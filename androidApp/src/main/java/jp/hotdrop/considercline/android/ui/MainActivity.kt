package jp.hotdrop.considercline.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observe()
    }

    private fun observe() {
        viewModel.appSettingLiveData.observe(this) {
            if (it.isInitialized()) {
                binding.userId.text = getString(R.string.splash_user_id_label, it.userId)
                binding.userId.isVisible = true
                // TODO Home画面へ遷移
            } else {
                binding.progressBar.isVisible = false
                binding.firstStartGroup.isVisible = true
                binding.startButton.setOnClickListener {
                    // TODO Login画面へ遷移
                }
            }
        }
        lifecycle.addObserver(viewModel)
    }
}
