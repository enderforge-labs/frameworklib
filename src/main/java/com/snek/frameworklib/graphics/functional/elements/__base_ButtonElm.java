package com.snek.frameworklib.graphics.functional.elements;

import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;




public sealed interface __base_ButtonElm extends Hoverable, Clickable permits FancyButtonElm, SimpleButtonElm {
    //Empty
}
