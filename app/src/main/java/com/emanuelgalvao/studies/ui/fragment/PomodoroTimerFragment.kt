package com.emanuelgalvao.studies.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.FragmentPomodoroTimerBinding
import com.emanuelgalvao.studies.model.enum.TimerState
import com.emanuelgalvao.studies.util.PrefUtil
import com.emanuelgalvao.studies.viewmodel.PomodoroTimerViewModel

class PomodoroTimerFragment : Fragment(), View.OnClickListener {

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

        //TODO: remove background timer and hide notification
    }

    override fun onPause() {
        super.onPause()

        if (mTimerState == TimerState.RUNNING) {
            mTimer.cancel()
            //TODO: start background timer and show notification
        } else if (mTimerState == TimerState.PAUSED) {
            mTimer.cancel()
            //TODO: show notification
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
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_start -> startTimer()
            R.id.fab_pause -> pauseTimer()
            R.id.fab_stop -> stopTimer()
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
            }
            TimerState.PAUSED -> {
                binding.fabStart.isEnabled = true
                binding.fabStart.isVisible = true
                binding.fabPause.isEnabled = false
                binding.fabPause.isVisible = false
                binding.fabStop.isEnabled = true
            }
            TimerState.STOPPED -> {
                binding.fabStart.isEnabled = true
                binding.fabStart.isVisible = true
                binding.fabPause.isEnabled = false
                binding.fabPause.isVisible = false
                binding.fabStop.isEnabled = false
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

}