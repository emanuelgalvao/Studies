package com.emanuelgalvao.studies.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.emanuelgalvao.studies.model.enum.TimerState
import com.emanuelgalvao.studies.util.NotificationUtil
import com.emanuelgalvao.studies.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimeExpired(context)

        PrefUtil.setTimerState(TimerState.STOPPED, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}