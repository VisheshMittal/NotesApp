package com.vm.vishunotesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

data class Outer(val id: Int, val dataFlow: StateFlow<String>)



fun main1() {
    val _myInnerFlow1 = MutableStateFlow<String>("a")
    val myInnerFlow1: StateFlow<String> = _myInnerFlow1

    val _myOuterFlow = MutableStateFlow<Outer>(Outer(0, myInnerFlow1))
    val myOuterFlow: StateFlow<Outer> = _myOuterFlow

    runBlocking {
        launch {
            myOuterFlow.collectLatest { outer ->
                println("collected outer with id: ${outer.id}, dataFlow: ${outer.dataFlow}")
                outer.dataFlow.collectLatest {
                    println("collected inner ${it}")
                }
            }
        }

        delay(200)
        _myInnerFlow1.value = "b"

        delay(100)
        val _myInnerFlow2 = MutableStateFlow<String>("z")
        val myInnerFlow2: StateFlow<String> = _myInnerFlow2
        _myOuterFlow.value = Outer(1, myInnerFlow2)

        delay(500)

        _myInnerFlow1.value = "c"
        _myInnerFlow1.value = "d"
        _myInnerFlow1.value = "e"

        _myInnerFlow2.value = "y"

    }
}

fun main() {
    val greeting by mutableStateOf("Hello")
    val person by mutableStateOf("Adam")


    // ...

    // Create a flow that will emit whenever our person-specific greeting changes
    val greetPersonFlow = snapshotFlow { "$greeting, $person" }

    // ...

    val collectionScope: CoroutineScope = TODO("Use your scope here")

    // Collect the flow and offer greetings!
    collectionScope.launch { greetPersonFlow.collect { println(greeting) } }
}