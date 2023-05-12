package com.example.projectmanager.ui.renameme



class Work(val name:String,val duration:Int,requiredWorks:List<Work>) {
    val requiredWorks=requiredWorks.toMutableList()
    var _durationPessimistic:Int?=null
    var _durationOptimistic:Int?=null
    var requiredWorksForTable:List<Work>
    var costToSpeedUp:Int? = null
    constructor(name:String,durationOptimistic:Int,durationAverage:Int,durationPessimistic:Int, requiredWorks:List<Work>):this(name,durationAverage,requiredWorks){
        _durationPessimistic=durationPessimistic
        _durationOptimistic=durationOptimistic
    }
    constructor(name:String,durationOptimistic:Int,durationPessimistic:Int, requiredWorks:List<Work>):this(name,durationOptimistic,requiredWorks){
        _durationPessimistic=durationPessimistic
        _durationOptimistic=durationOptimistic
    }
    init {
        requiredWorksForTable=requiredWorks.toList()
    }
}

fun List<Work>.toStr():String{
    return this.joinToString(", ") { it.name }
}