package com.alaazarifa.examplepincode

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.alaazarifa.examplepincode.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var inputs: MutableList<EditText> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            manageInputs()

            switchBtn.setOnCheckedChangeListener { button, isCheck ->
                hideKeyboard(binding.root)
                if (isCheck) {
                    seekbar.isEnabled = false
                    seekbar.alpha = 0.5f
                    input.apply {
                        inputs.forEach {
                            it.clear()
                            it.clearFocus()
                            it.background = drawable(R.drawable.selector_input_state_pin_circle)

                        }
                    }
                }
                else {
                    seekbar.isEnabled = true
                    seekbar.alpha = 1f
                    seekbar.progress = 10
                    inputs.forEach {
                        it.clear()
                        it.clearFocus()
                        it.background = drawable(R.drawable.selector_input_state_pin)
                    }
                }
            }

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    raduisValue.text = "$progress dp"
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        inputs.forEach {
                            val background = it.background as StateListDrawable
                            val bgShape1 = background.getStateDrawable(0) as GradientDrawable
                            val bgShape2 = background.getStateDrawable(1) as GradientDrawable
                            bgShape1.mutate()
                            bgShape1.cornerRadius = progress.toFloat()
                            bgShape2.mutate()
                            bgShape2.cornerRadius = progress.toFloat()
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            });

            { input.pin1.showKeyboard() }.withDelay()

        }
    }


    // utility method to check if the code isn't fully entered
    private fun ActivityMainBinding.isCodeEmpty(): Boolean {
        return (input.pin1.text.toString().isEmpty()
                || input.pin2.text.toString().isEmpty()
                || input.pin3.text.toString().isEmpty()
                || input.pin4.text.toString().isEmpty()
                )
    }

    // utility method to get the full code
    private fun ActivityMainBinding.getCode(): String {
        return "${input.pin1.text}${input.pin2.text}${input.pin3.text}${input.pin4.text}"
    }

    // handling Inputs listeners
    private fun ActivityMainBinding.manageInputs() {
        input.apply {

            inputs.add(pin1)
            inputs.add(pin2)
            inputs.add(pin3)
            inputs.add(pin4)

            // handle "Delete" button click on soft-keyboard
            pin1.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_DEL) pin1.setText("")
                false
            }
            pin2.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (pin2.text.toString().isEmpty() && keyEvent.action == 0) {
                        { pin1.requestFocus() }.withDelay(10)
                    } else pin2.setText("")
                }
                false
            }
            pin3.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (pin3.text.toString().isEmpty() && keyEvent.action == 0) {
                        { pin2.requestFocus() }.withDelay(10)
                    } else pin3.setText("")
                }
                false
            }
            pin4.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (pin4.text.toString().isEmpty() && keyEvent.action == 0) {
                        { pin3.requestFocus() }.withDelay(10)
                    } else pin4.setText("")
                }
                false
            }

            // observing inputs to auto-focus the next pin once the current input is entered
            pin1.setTextWatcher {
                if (it.length == 1) {
                    { pin2.requestFocus() }.withDelay(10)
                }
            }
            pin2.setTextWatcher {
                if (it.length == 1) {
                    { pin3.requestFocus() }.withDelay(10)
                }
            }
            pin3.setTextWatcher {
                if (it.length == 1) {
                    { pin4.requestFocus() }.withDelay(10)
                }
            }
            pin4.setTextWatcher {
                if (it.length == 1) {
                    {
                        hideKeyboard(pin4)
                        if (isCodeEmpty().not()) {
                            pin4.clearFocus()
                            "Code Entered!".toast(this@MainActivity)
                        }
                    }.withDelay(10)
                }
            }
        }
    }


}
