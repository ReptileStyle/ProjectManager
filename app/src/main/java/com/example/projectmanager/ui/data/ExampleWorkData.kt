package com.example.projectmanager.ui.data

import com.example.projectmanager.ui.renameme.Work

val work1 = Work("b1",3,5,8, listOf())
val work2 = Work("b2",4,9,10, listOf())
val work3 = Work("b3",1,2,6, listOf())
val work4 = Work("b4",1,7,9, listOf(work1))
val work5 = Work("b5",1,4,5, listOf(work1))
val work6 = Work("b6",1,1,2, listOf(work3))
val work7 = Work("b7",1,2,4, listOf(work2,work5,work6))
val work8 = Work("b8",4,5,13, listOf(work2,work5,work6))
val work9 = Work("b9",1,2,8, listOf(work4,work7))
val work10 = Work("b10",6,8,17, listOf(work3))
val work11 = Work("b11",2,8,10, listOf(work2,work5,work6,work10))
//val work12 = Work("b12",7, listOf(work9,work7,work6,work10))
//val work13 = Work("b13",7, listOf(work6,work10))
//val work14 = Work("b14",7, listOf(work6,work10))
//val work15 = Work("b15",7, listOf(work14))
//val work16 = Work("b16",7, listOf(work15))
//val work17 = Work("b17",7, listOf(work14,work15))
//val work18 = Work("b18",7, listOf(work13))
//val work19 = Work("b19",7, listOf(work13,work16))
//val work20 = Work("b20",7, listOf(work19))
//val work21 = Work("b21",7, listOf(work20))
//val work22 = Work("b22",7, listOf(work21))


val exampleWorkList:MutableList<Work> = mutableListOf(
    work1,
    work2,
    work3,
    work4,
    work5,
    work6,
    work7,
    work8,
    work9,
    work10,
    work11,
//    work12,
//    work13,
//    work14,
//    work15,
//    work16,
//    work17,
//    work18,
//    work19,
//    work20,
//    work21,
//    work22,

)