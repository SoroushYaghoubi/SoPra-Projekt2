package gui

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.CheckBox
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.style.BorderRadius
import util.*

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
    posX : Int = 0, posY : Int = 0, text : String = "Text Field", prompt : String = ""
) : TextField(
    posX = posX,
    posY = posY,
    width = 600,
    height = 110,
    text = text,
    prompt = prompt,
    font = Font(48.0, Color(PRIMARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(TERTIARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)

class TurnLabel (
    posX : Int = 0, posY : Int = 0, text : String = "1"
) : Label(
    posX = posX,
    posY = posY,
    width = 110,
    height = 110,
    text = text,
    font = Font(48.0, Color(PRIMARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(TERTIARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)

class CheckBoxStyle1 (
    posX : Int = 0, posY : Int = 0,
) : CheckBox(
    posX = posX,
    posY = posY,
    width = 110,
    height = 110,
    font = Font(48.0, Color(PRIMARY_COLOUR), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(TERTIARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)

class SquareButton(
    posX : Int = 0, posY : Int = 0,
) : Button(
    posX = posX,
    posY = posY,
    width = 110,
    height = 110,
    text = "X",
    font = Font(48.0, Color(0x000000), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(TERTIARY_COLOUR)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)

class ColourButton(
    posX : Int = 0, posY : Int = 0,
) : Button(
    posX = posX,
    posY = posY,
    width = 90,
    height = 90,
    text = "",
    font = Font(48.0, Color(0x000000), "Arial Black", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(COLOUR_RED)).apply {
        style.borderRadius = BorderRadius(20.0)
    }
)
