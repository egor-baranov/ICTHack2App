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

        RequestWorker.getNotificationsOfUser(
            (requireActivity() as MainActivity).userData.id,
            { notificationList -> fillNotifications(notificationList) }
        )

        fillNotifications(listOf())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        newNotificationView.findViewWithTag<TextView>("title").text =
            when (notification.type) {
                NotificationType.NEW_REPLY -> "Запрос"
                NotificationType.ACCEPTED_REPLY -> "Принято"
                NotificationType.DENIED_REPLY -> "Отклонено"
                NotificationType.WAIT_REPLY -> "В ожидании"
            }

        fun fillSomeData(user: User, project: Project) {
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
            newNotificationView
                .findViewWithTag<ConstraintLayout>("background")
                .setOnClickListener {
                    val activity = (requireActivity() as MainActivity)

                    if (notification.type == NotificationType.NEW_REPLY)
                        activity.showBottomSheet(user)
//                    } else {
////                        activity.fillProjectInfo(project)
////                        activity.uiContext = MainActivity.UiContext.OPENED_PROJECT_SCREEN
//                        activity.binding.projectScreen.root.visibility = View.VISIBLE
////                        activity.performTransformAnimation(
////                            newNotificationView.rootView,
////                            activity.binding.projectScreen.root
////                        )
//
////                        activity.binding.fabAddProject.visibility = View.GONE
//
////                        activity.binding.projectScreen.buttonBack.setOnClickListener {
////                            activity.performTransformAnimation(
////                                activity.binding.projectScreen.root,
////                                it
////                            )
////                            activity.binding.fabAddProject.visibility = View.VISIBLE
////                        }
//                    }
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

//        RequestWorker.getUserById(
//            notification.toUserId
//        ) { user ->
//            RequestWorker.getReplyById(
//                notification.replyId
//            ) { reply ->
//                RequestWorker.getProjectById(
//                    reply.projectId
//                ) { project ->
//                    requireActivity().runOnUiThread {
//                        fillSomeData(user, project, reply)
//                    }
//                }
//            }
//        }

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