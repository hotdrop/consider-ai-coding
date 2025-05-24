package jp.hotdrop.considercline.android.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.databinding.ActivityStartBinding
import jp.hotdrop.considercline.android.ui.home.HomeActivity

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStartBinding.inflate(layoutInflater) }
    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observe()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        binding.emailEditText.addTextChangedListener {
            viewModel.onEmailChanged(it.toString())
        }

        binding.nicknameEditText.addTextChangedListener {
            viewModel.onNickNameChanged(it.toString())
        }

        binding.registerButton.setOnClickListener {
            viewModel.save()
        }
    }

    private fun observe() {
        viewModel.uiStateLiveData.observe(this) {
            when {
                it.isComplete -> navigationToHome()
                it.isLoading -> {
                    binding.registerButton.isVisible = false
                    binding.progressBar.isVisible = true
                }
                else -> {
                    binding.registerButton.isVisible = true
                    binding.progressBar.isVisible = false
                }
            }
        }
        viewModel.errorLiveData.observe(this) {
            // TODO エラーダイアログポップアップ
        }
        lifecycle.addObserver(viewModel)
    }

    private fun navigationToHome() {
        HomeActivity.start(this)
        finish()
    }

    companion object {
        fun startActivity(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, StartActivity::class.java))
    }
}