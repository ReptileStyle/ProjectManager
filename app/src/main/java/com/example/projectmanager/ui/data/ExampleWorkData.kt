package com.example.projectmanager.ui.data

import com.example.projectmanager.ui.renameme.Work

val work1 = Work("b1",5, listOf())
val work2 = Work("b2",8, listOf())
val work3 = Work("b3",3, listOf())
val work4 = Work("b4",6, listOf(work1))
val work5 = Work("b5",4, listOf(work1))
val work6 = Work("b6",1, listOf(work3))
val work7 = Work("b7",2, listOf(work2,work5,work6))
val work8 = Work("b8",6, listOf(work2,work5,work6))
val work9 = Work("b9",3, listOf(work5,work7))
val work10 = Work("b10",9, listOf(work3))
val work11 = Work("b11",7, listOf(work2,work5,work6,work10))

val exampleWorkList:List<Work> = listOf(
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
)