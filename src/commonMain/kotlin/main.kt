import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.geom.shape.Shape2d
import com.soywiz.korma.interpolation.*

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {

//	val circle = Circle(radius = 20.0, fill = Colors.GREEN).xy(100, 100)
//	addChild(circle)
	// ^^ code above does the same as:

	circle(radius = 20.0, fill = Colors.GREEN).xy(100, 100)  // can use this syntax with shapes and views that can be added to screen

    solidRect(width = 100.0, height = 100.0, Colors.GOLD).xy(110, 100)
}





// Hello World Sample Code (was inside main function):

//	val minDegrees = (-16).degrees
//	val maxDegrees = (+16).degrees
//
//	val image = image(resourcesVfs["korge.png"].readBitmap()) {
//		rotation = maxDegrees
//		anchor(.5, .5)
//		scale(.8)
//		position(256, 256)
//	}
//
//	while (true) {
//		image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
//		image.tween(image::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
//	}