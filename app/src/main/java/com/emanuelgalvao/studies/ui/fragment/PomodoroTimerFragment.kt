package com.emanuelgalvao.studies.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.FragmentPomodoroTimerBinding
import com.emanuelgalvao.studies.model.enum.TimerState
import com.emanuelgalvao.studies.service.receiver.TimerExpiredReceiver
import com.emanuelgalvao.studies.util.NotificationUtil
import com.emanuelgalvao.studies.util.PrefUtil
import com.emanuelgalvao.studies.viewmodel.PomodoroTimerViewModel
import java.util.*

class PomodoroTimerFragment : Fragment(), View.OnClickListener {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    private lateinit var mTimer: CountDownTimer
    private var mTimerLengthSeconds = 0L
    private var mTimerState = TimerState.STOPPED
    private var mSecondsRemaining = 0L

    private var _binding: FragmentPomodoroTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: PomodoroTimerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentPomodoroTimerBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider(this).get(PomodoroTimerViewModel::class.java)

        listeners()

        return view
    }

    override fun onResume() {
        super.onResume()

        initializeTimer()

        removeAlarm(requireContext())
        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onPause() {
        super.onPause()

        if (mTimerState == TimerState.RUNNING) {
            mTimer.cancel()
            val wakeUpTime = setAlarm(requireContext(), nowSeconds, mSecondsRemaining)
            NotificationUtil.showTimerRunning(requireContext(), wakeUpTime)
        } else if (mTimerState == TimerState.PAUSED) {
            mTimer.cancel()
            NotificationUtil.showTimerPaused(requireContext())
        }

        PrefUtil.setPreviousTimerLengthSeconds(mTimerLengthSeconds, requireContext())
        PrefUtil.setSecondsRemaining(mSecondsRemaining, requireContext())
        PrefUtil.setTimerState(mTimerState, requireContext())
    }

    private fun initializeTimer() {

        mTimerState = PrefUtil.getTimerState(requireContext())

        if (mTimerState == TimerState.STOPPED) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        mSecondsRemaining = if (mTimerState == TimerState.RUNNING || mTimerState == TimerState.PAUSED) {
            PrefUtil.getSecondsRemaining(requireContext())
        } else {
            mTimerLengthSeconds
        }

        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext())
        if (alarmSetTime > 0) {
            mSecondsRemaining -= nowSeconds - alarmSetTime
        }

        if (mSecondsRemaining <= 0) {
            onTimerFinished()
        }
        if (mTimerState == TimerState.RUNNING) {
            startTimer()
        }

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        mTimerState = TimerState.STOPPED

        setNewTimerLength()

        binding.progressTimer.progress = 0

        PrefUtil.setSecondsRemaining(mTimerLengthSeconds, requireContext())
        mSecondsRemaining = mTimerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun listeners() {
        binding.fabStart.setOnClickListener(this)
        binding.fabPause.setOnClickListener(this)
        binding.fabStop.setOnClickListener(this)
        binding.buttonTimerSettings.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_start -> startTimer()
            R.id.fab_pause -> pauseTimer()
            R.id.fab_stop -> stopTimer()
            R.id.button_timer_settings -> openTimerSettings()
        }
    }

    private fun startTimer() {
        mTimerState = TimerState.RUNNING

        mTimer = object: CountDownTimer(mSecondsRemaining * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mSecondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
            override fun onFinish() {
                onTimerFinished()
            }
        }.start()

        updateButtons()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(requireContext())
        mTimerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressTimer.max = mTimerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        mTimerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext())
        binding.progressTimer.max = mTimerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = mSecondsRemaining / 60
        val secondsInMinuteUntilFinished = mSecondsRemaining - minutesUntilFinished * 60
        var secondStr = secondsInMinuteUntilFinished.toString()

        val minutesStr = if (minutesUntilFinished >= 10) "$minutesUntilFinished" else "0${minutesUntilFinished}"
        secondStr = if (secondStr.length == 1) "0${secondStr}" else secondStr

        binding.textTimer.text = "${minutesStr}:${secondStr}"
        binding.progressTimer.progress = (mTimerLengthSeconds - mSecondsRemaining).toInt()
    }

    private fun updateButtons() {
        when (mTimerState) {
            TimerState.RUNNING -> {
                binding.fabStart.isEnabled = false
                binding.fabStart.isVisible = false
                binding.fabPause.isEnabled = true
                binding.fabPause.isVisible = true
                binding.fabStop.isEnabled = true
                binding.fabStop.isVisible = true
                binding.buttonTimerSettings.isVisible = false
            }
            TimerState.PAUSED -> {
                binding.fabStart.isEnabled = true
                binding.fabStart.isVisible = true
                binding.fabPause.isEnabled = false
                binding.fabPause.isVisible = false
                binding.fabStop.isEnabled = true
                binding.fabStop.isVisible = true
                binding.buttonTimerSettings.isVisible = false
            }
            TimerState.STOPPED -> {
                binding.fabStart.isEnabled = true
                binding.fabStart.isVisible = true
                binding.fabPause.isEnabled = false
                binding.fabPause.isVisible = false
                binding.fabStop.isEnabled = false
                binding.fabStop.isVisible = false
                binding.buttonTimerSettings.isVisible = true
            }
        }
    }

    private fun pauseTimer() {
        mTimer.cancel()
        mTimerState = TimerState.PAUSED
        updateButtons()
    }

    private fun stopTimer() {
        mTimer.cancel()
        onTimerFinished()
    }

    @SuppressLint("CheckResult")
    private fun openTimerSettings() {
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.timer_settings)
            input(hintRes = R.string.time_minutes, waitForPositiveButton = true, inputType = InputType.TYPE_CLASS_NUMBER, maxLength = 2, prefill = PrefUtil.getTimerLength(requireContext()).toString()) { dialog, text ->
                val inputField = dialog.getInputField()
                val isValid = text.isNotEmpty()

                inputField.error = if (isValid) null else "Preencha o tempo de estudo!"
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
            }
            positiveButton(null, "Salvar") {
                PrefUtil.setTimerLength(getInputField().text.toString().toLong(), requireContext())
                onResume()
            }
            negativeButton(null, "Cancelar")
        }
    }

}