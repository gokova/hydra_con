package com.hydra.hydracon.firebase.model

data class TickOperation(var type: TickOperationType, var amount: Int)

enum class TickOperationType {
    LOAD,
    USE
}
