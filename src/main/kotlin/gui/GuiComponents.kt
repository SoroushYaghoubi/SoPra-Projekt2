package gui

import util.PRIMARY_COLOUR
import util.SECONDARY_COLOUR
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.style.BorderRadius
import util.QUATERNARY_COLOUR
import util.TERTIARY_COLOUR

class ButtonStyle1(
    posX : Int = 0, posY: Int = 0, text: String = "Button"
) : Button(
    posX = posX,
    posY = posY,
    width = 450,
    height = 100,
    text = text,
    font = Font(24.0, Color(PRIMARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)

class ButtonStyle2(
    posX : Int = 0, posY : Int = 0, text : String = "Button"
) : Button(
    posX = posX,
    posY = posY,
    width = 450,
    height = 100,
    text = text,
    font = Font(24.0, Color(TERTIARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(QUATERNARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)


class LabelStyle1(
    posX : Int = 0, posY : Int = 0, text : String = "Label"
) : Label(
    posX = posX,
    posY = posY,
    width = 450,
    height = 100,
    text = text,
    font = Font(24.0, Color(PRIMARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)

class TextFieldStyle1(
    posX : Int = 0, posY : Int = 0, text : String = "Text Field"
) : TextField(
    posX = posX,
    posY = posY,
    width = 450,
    height = 100,
    text = text,
    font = Font(24.0, Color(PRIMARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)
