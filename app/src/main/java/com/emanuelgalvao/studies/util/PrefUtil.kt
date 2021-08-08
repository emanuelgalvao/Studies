package com.emanuelgalvao.studies.util

import android.content.Context
import android.preference.PreferenceManager
import com.emanuelgalvao.studies.model.enum.TimerState

class PrefUtil {

    companion object {

        private const val TIMER_LENGTH_ID = "com.emanuelgalvao.studies.timer_length"

        fun getTimerLength(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(TIMER_LENGTH_ID, 25)
        }

        fun setTimerLength(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(TIMER_LENGTH_ID, seconds)
            editor.apply()
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.emanuelgalvao.studies.previous_timer_length"

        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.emanuelgalvao.studies.timer_state"

        fun getTimerState(context: Context): TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal =  preferences.getInt(TIMER_STATE_ID, 0)
            return TimerState.values()[ordinal]
        }

        fun setTimerState(timerState: TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = timerState.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "com.emanuelgalvao.studies.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(secondsRemaining: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, secondsRemaining)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.emanuelgalvao.studies.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}