package service

import edu.udo.cs.sopra.ntf.ColorTypeMessage
import entity.ColorType

fun ColorTypeMessage.toColor() : ColorType {
    return when (this) {
        ColorTypeMessage.BLUE -> ColorType.BLUE
        ColorTypeMessage.PURPLE -> TODO()
        ColorTypeMessage.BLACK -> TODO()
        ColorTypeMessage.RED -> TODO()
    }
}

fun ColorType.toColorMessage() : ColorTypeMessage {
    return when (this) {
        ColorType.BLUE -> ColorTypeMessage.BLUE
        ColorType.RED -> TODO()
        ColorType.PURPLE -> TODO()
        ColorType.BLACK -> TODO()
    }
}
