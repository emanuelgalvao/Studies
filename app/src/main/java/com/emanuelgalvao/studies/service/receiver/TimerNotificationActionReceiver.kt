package com.emanuelgalvao.studies.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.emanuelgalvao.studies.model.enum.TimerState
import com.emanuelgalvao.studies.ui.fragment.PomodoroTimerFragment
import com.emanuelgalvao.studies.util.Constants
import com.emanuelgalvao.studies.util.NotificationUtil
import com.emanuelgalvao.studies.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            Constants.ACTION_STOP -> {
                PomodoroTimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerState.STOPPED, context)
                NotificationUtil.hideTimerNotification(context)
            }
            Constants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = PomodoroTimerFragment.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                PomodoroTimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerState.PAUSED, context)
                NotificationUtil.showTimerPaused(context)
            }
            Constants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = PomodoroTimerFragment.setAlarm(context, PomodoroTimerFragment.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.RUNNING, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            Constants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = PomodoroTimerFragment.setAlarm(context, PomodoroTimerFragment.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.RUNNING, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}