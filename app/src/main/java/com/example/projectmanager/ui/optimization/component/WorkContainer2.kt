package com.example.projectmanager.ui.optimization.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.projectmanager.ui.renameme.Work

@Composable
fun WorkContainer2(
    modifier: Modifier,
    work:Work?,
    monteCarloInfo: Triple<Int?,Int,Int?>,
    onClick:(()->Unit)? = null
) {

    if(work!=null) {
        Row(modifier.clickable(enabled = onClick!=null) { onClick?.invoke() }) {
            Text(text = work.name, modifier = Modifier.weight(1.5f), fontSize = 10.sp)
            Text(text = monteCarloInfo.first?.toString() ?: "", modifier = Modifier.weight(1f), fontSize = 10.sp)
            Text(text = monteCarloInfo.second.toString(), modifier = Modifier.weight(1f), fontSize = 10.sp)
            Text(text = monteCarloInfo.third?.toString() ?: "", modifier = Modifier.weight(1f), fontSize = 10.sp)
            Text(
                text = work.requiredWorks.joinToString(separator = ", ") { it.name },
                modifier = Modifier.weight(2.5f), fontSize = 10.sp
            )
        }
    }else{
        Row(modifier) {
            Text(text = "Название", modifier = Modifier.weight(1.5f), fontSize = 10.sp)
            Text(text = "Время опт.", modifier = Modifier.weight(1f), fontSize = 10.sp)
            Text(text = "Время нв.", modifier = Modifier.weight(1f), fontSize = 10.sp)
            Text(text ="Время пес.", modifier = Modifier.weight(1f), fontSize = 10.sp)
            Text(
                text = "Требуемые работы",
                modifier = Modifier.weight(2.5f), fontSize = 10.sp
            )
        }
    }
}