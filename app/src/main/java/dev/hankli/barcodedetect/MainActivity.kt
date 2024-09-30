package dev.hankli.barcodedetect

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class MainActivity : AppCompatActivity() {
    private lateinit var viewBarcode: DecoratedBarcodeView
    private lateinit var viewTorch: Button
    private lateinit var viewScan: Button
    private lateinit var viewResult: TextView

    private lateinit var captureManager: CaptureManager
    private var torchState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewBarcode = findViewById(R.id.view_barcode)
        viewTorch = findViewById(R.id.view_torch)
        viewScan = findViewById(R.id.view_scan)
        viewResult = findViewById(R.id.view_result)

        captureManager = CaptureManager(this, viewBarcode)
        captureManager.initializeFromIntent(intent, savedInstanceState)

        viewScan.setOnClickListener {
            viewResult.text = "..."
            viewBarcode.decodeSingle { result ->
                viewResult.text = result.text
                vibrate()
            }
        }

        viewTorch.setOnClickListener {
            torchState = if (torchState) {
                viewBarcode.setTorchOff()
                false
            } else {
                viewBarcode.setTorchOn()
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
