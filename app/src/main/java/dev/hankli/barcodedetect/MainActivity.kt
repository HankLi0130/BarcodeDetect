package dev.hankli.barcodedetect

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var captureManager: CaptureManager
    private var torchState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captureManager = CaptureManager(this, view_barcode)
        captureManager.initializeFromIntent(intent, savedInstanceState)

        view_scan.setOnClickListener {
            view_result.text = "..."
            view_barcode.decodeSingle { result ->
                view_result.text = result.text
                vibrate()
            }
        }

        view_torch.setOnClickListener {
            torchState = if (torchState) {
                view_barcode.setTorchOff()
                false
            } else {
                view_barcode.setTorchOn()
                true
            }
        }
    }

    private fun vibrate() {
        val vibrator: Vibrator? = getSystemService(Vibrator::class.java)

        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(100)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }
}
