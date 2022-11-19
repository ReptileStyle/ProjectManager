package com.example.projectmanager.ui.renameme



class Work(val name:String,val duration:Int,val requiredWorks:List<Work>) {

}

fun List<Work>.toStr():String{
    return this.joinToString(", ") { it.name }
}