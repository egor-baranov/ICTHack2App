package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.databinding.FragmentNotificationsBinding
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.Notification
import com.kepler88d.icthack2app.model.data.Project
import com.kepler88d.icthack2app.model.data.User
import com.kepler88d.icthack2app.model.enumerations.NotificationType


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            (activity as MainActivity).binding.viewpagerMain.setCurrentItem(1, true)
        }

        RequestWorker.getNotificationsOfUser(
            (requireActivity() as MainActivity).userData.id,
            { notificationList -> fillNotifications(notificationList) }
        )

        fillNotifications(listOf())
    }

    private fun addBackButtonListener() {
        binding.buttonBack.setOnClickListener {
            (activity as MainActivity).binding.viewpagerMain.setCurrentItem(
                0, true
            )
        }
    }

    private fun addNotificationView(notification: Notification) {
        val newNotificationView: View = LayoutInflater.from(this.context)
            .inflate(R.layout.item_reply, null, false)

        fun fillSomeData(user: User, project: Project) {
            newNotificationView.findViewWithTag<TextView>("title").setTextColor(
                context!!.getColor(
                    when (notification.type) {
                        NotificationType.NEW_REPLY -> R.color.state_request
                        NotificationType.ACCEPTED_REPLY -> R.color.state_accepted
                        NotificationType.DENIED_REPLY -> R.color.state_rejected
                        NotificationType.WAIT_REPLY -> R.color.state_waiting
                    }
                )
            )

            newNotificationView.findViewWithTag<TextView>("title").text =
                when (notification.type) {
                    NotificationType.NEW_REPLY -> "Запрос"
                    NotificationType.ACCEPTED_REPLY -> "Принято"
                    NotificationType.DENIED_REPLY -> "Отклонено"
                    NotificationType.WAIT_REPLY -> "В ожидании"
                }

            newNotificationView.findViewWithTag<TextView>("project").text = project.name
            newNotificationView.findViewWithTag<TextView>("text").text =
                when (notification.type) {
                    NotificationType.NEW_REPLY ->
                        "Пользователь ${user.fullName()} окликнулся на ваш проект ${project.name}"
                    NotificationType.ACCEPTED_REPLY -> "Ваш отклик на проект " +
                            "${project.name} принят"
                    NotificationType.DENIED_REPLY -> "К сожалению, ваш отклик на проект " +
                            "${project.name} был отклонен"
                    NotificationType.WAIT_REPLY -> "Скоро ваш отклик на проект " +
                            "${project.name} будет рассмотрен"
                }
        }

        RequestWorker.getUserById(
            notification.toUserId
        ) { user ->
            RequestWorker.getProjectById(
                3
            ) { project ->
                requireActivity().runOnUiThread {
                    fillSomeData(user, project)
                }
            }
        }

        binding.repliesListView.addView(newNotificationView)
    }

    private fun fillNotifications(notificationList: List<Notification>) {
        Log.d("notificationList", notificationList.toString())
        notificationList.forEach { addNotificationView(it) }
        addNotificationView(
            Notification(
                id = 1,
                toUserId = 312314,
                title = "hello",
                text = "ok",
                type = NotificationType.ACCEPTED_REPLY,
                replyId = 2
            )
        )
        addNotificationView(
            Notification(
                id = 3,
                toUserId = 312314,
                title = "",
                text = "",
                type = NotificationType.NEW_REPLY,
                replyId = 3
            )
        )
    }
}