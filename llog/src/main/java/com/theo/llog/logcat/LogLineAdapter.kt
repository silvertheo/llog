package com.theo.llog.logcat

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.theo.llog.R
import com.theo.llog.logcat.util.ColorUtil

class LogLineAdapter(
    private var context: Context,
) : RecyclerView.Adapter<LogLineAdapter.LogLineViewHolder>() {
    private val dataSet = arrayListOf<LogLine>()
    private val originalLogLines = arrayListOf<LogLine>()
    private var limitLevel = Log.VERBOSE

    fun addLog(logLine: LogLine, refresh: Boolean) {
        originalLogLines.add(logLine)
        if (logLine.logLevel >= limitLevel) {
            dataSet.add(logLine)
        }
        if (refresh) {
            notifyItemInserted(dataSet.size)
        }
    }

    fun setLimitLevel(limit: Int) {
        limitLevel = limit
        dataSet.clear()
        dataSet.addAll(originalLogLines.filter { it.logLevel >= limitLevel })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogLineViewHolder {
        val inflater = LayoutInflater.from(context)
        return LogLineViewHolder(inflater.inflate(R.layout.llog_item_log, parent, false))
    }

    override fun onBindViewHolder(holder: LogLineViewHolder, position: Int) {
        val logLine = dataSet[position]
        holder.run {
            message.text = logLine.logOutput
            level.text = logLine.logLevelText
            tag.text = logLine.tag
            pid.text = logLine.processId.toString()
            time.text = logLine.timestamp

            val textColor = ColorUtil.getTextColor(context, logLine.logLevel)
            val levelColor = ColorUtil.getLevelColor(context, logLine.logLevel)
            message.setTextColor(textColor)
            level.setBackgroundColor(levelColor)
            tag.setBackgroundColor(levelColor)

            switchExpanded(holder, logLine)
            itemView.setOnClickListener {
                logLine.isExpanded = !logLine.isExpanded
                switchExpanded(holder, logLine)
            }
        }
    }

    private fun switchExpanded(holder: LogLineViewHolder, logLine: LogLine) {
        holder.run {
            if (logLine.isExpanded && logLine.processId != -1) {
                itemView.setBackgroundColor(Color.parseColor("#F8F8F8"))
                detail.visibility = View.VISIBLE
                space.visibility = View.VISIBLE
                message.isSingleLine = false
            } else {
                itemView.setBackgroundColor(Color.WHITE)
                detail.visibility = View.GONE
                space.visibility = View.GONE
                message.isSingleLine = true
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class LogLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var detail: ViewGroup = itemView.findViewById(R.id.llog_detail_info)
        var space: View = itemView.findViewById(R.id.llog_space_bottom)
        var message: TextView = itemView.findViewById(R.id.llog_message_text)
        var level: TextView = itemView.findViewById(R.id.llog_level_text)
        var tag: TextView = itemView.findViewById(R.id.llog_tag_text)
        var pid: TextView = itemView.findViewById(R.id.llog_pid_text)
        var time: TextView = itemView.findViewById(R.id.llog_time_text)
    }
}