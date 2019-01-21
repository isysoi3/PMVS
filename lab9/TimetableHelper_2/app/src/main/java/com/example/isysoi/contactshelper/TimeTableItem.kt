package com.example.isysoi.contactshelper

import java.io.Serializable

class TimeTableItem(val id: Int,
                    val name: String,
                    val startTime: String,
                    val finishTime: String,
                    val teacherFIO: String,
                    val room: Int): Serializable
