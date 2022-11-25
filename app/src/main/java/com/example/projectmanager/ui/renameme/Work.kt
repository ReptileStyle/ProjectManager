package com.example.projectmanager.ui.renameme



class Work(val name:String,val duration:Int,requiredWorks:List<Work>) {
    val requiredWorks=requiredWorks.toMutableList()

}

fun List<Work>.toStr():String{
    return this.joinToString(", ") { it.name }
}