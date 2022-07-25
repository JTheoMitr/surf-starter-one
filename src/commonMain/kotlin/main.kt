import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.time.delay
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*
import kotlin.random.Random

suspend fun main() = Korge(width = 1024, height = 768, bgcolor = Colors["#2b2b2b"]) {

	// Some Abstract Values
	val buffer = 40
    val minDegrees = (-16).degrees
	val maxDegrees = (+16).degrees

	// Sprite and Animation Control
	val waveSprites = resourcesVfs["wave_break_demo.xml"].readAtlas()
	val breakAnimation = waveSprites.getSpriteAnimation("wave")

	val surferSprites = resourcesVfs["surfer_boi.xml"].readAtlas()
	val idleAnimation = surferSprites.getSpriteAnimation("surfer")

	// Establish Background Variable
	val bgField = RoundRect(width, height, 5.0, fill = Colors["#084762"]).apply {
		x = 0.0
		y = 0.0
	}

	// Establish WaveBreak for Level Background
	val waveBreak = sprite(breakAnimation) {
		scaledHeight = 1670.0
		scaledWidth = 300.0
		anchor(.5, .5)
		visible = true
	}

	// Set Stage for Components
	addChild(bgField)
	addChild(waveBreak)

	waveBreak.playAnimationLooped(spriteDisplayTime = 200.milliseconds)

	// Add Components to the Stage

	val waypoint = image(resourcesVfs["ocean_waypoint_two.png"].readBitmap()) {
		anchor(.5, .5)
		scale(.6)
		visible = false
	}

	val surfer = sprite(idleAnimation) {
		anchor(.5, .5)
		scale(.9)
		position(200.0, 300.0)
	}
	surfer.playAnimationLooped(spriteDisplayTime = 200.milliseconds)

	val jellySchool = Array<Image>(1) {
		image(resourcesVfs["jellyfish_1.png"].readBitmap()) {
			anchor(.5, .5)
			scale(.4)
			visible = false
		}
	}

	bgField.onClick {
		println("clicked!")

		val target = it.currentPosLocal
		waypoint.visible = true
		waypoint.pos = target
		surfer.tweenAsync(surfer::x[surfer.x, target.x], time = 2.seconds, easing = Easing.EASE_IN_OUT)
		surfer.tweenAsync(surfer::y[surfer.y, target.y], time = 2.seconds, easing = Easing.EASE_IN_OUT)

		surfer.tween(surfer::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
		surfer.tween(surfer::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
	}

	while (true) {


		jellySchool.forEach {
			if(!it.visible || it.pos.y > height) {
				delay((Random.nextInt(1, 3)).seconds)
				val jellyX = Random.nextInt(buffer, (width.toInt() - buffer)).toDouble()
				it.visible = true
				it.position(jellyX.toDouble(), -5.0)
				it.moveTo(jellyX, height + buffer, 3.seconds, Easing.EASE_IN)
			}
		}
	}

    // Basic Shapes and Images:
//	val circle = Circle(radius = 20.0, fill = Colors.GREEN).xy(100, 100)
//	addChild(circle)
	// ^^ code above does the same as:
//	circle(radius = 20.0, fill = Colors.GREEN).xy(100, 100)  // can use this syntax with shapes and views that can be added to screen
//
//
//    solidRect(width = 100.0, height = 100.0, Colors.GOLD).xy(110, 110)  // the later a view is added to it's parent, the higher it is in the drawing stack
//
//    val bitmap = resourcesVfs["korge.png"].readBitmap()
//    image(bitmap).scale(.3).apply {
//       // rotation = (+50).degrees
//        alpha = 0.5
//    }

// Sprites and SpriteAnimations:





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