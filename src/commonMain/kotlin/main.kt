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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

	// PURPLE Jellyfish
	val jellyOneSprites = resourcesVfs["jellyfish_one.xml"].readAtlas()
	val jellyOneAnimation = jellyOneSprites.getSpriteAnimation("jelly")

	// GREEN Jellyfish
	val jellyTwoSprites = resourcesVfs["jellyfish_two.xml"].readAtlas()
	val jellyTwoAnimation = jellyTwoSprites.getSpriteAnimation("jelly")

	val canOneSprites = resourcesVfs["oil_can_one.xml"].readAtlas()
	val canOneAnimation = canOneSprites.getSpriteAnimation("img")

	val garbageBagSprites = resourcesVfs["garbage_bag_one.xml"].readAtlas()
	val garbageBagAnimation = garbageBagSprites.getSpriteAnimation("img")

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

	val heartImgOne = image(resourcesVfs["pixel_heart_one.png"].readBitmap()) {
		anchor(.5,.5)
		scale(.03)
		position(actualVirtualRight - 140, actualVirtualTop.toDouble() + 35)
	}

	val heartImgTwo = image(resourcesVfs["pixel_heart_one.png"].readBitmap()) {
		anchor(.5,.5)
		scale(.03)
		position(actualVirtualRight - 100, actualVirtualTop.toDouble() + 35)
	}

	val heartImgThree = image(resourcesVfs["pixel_heart_one.png"].readBitmap()) {
		anchor(.5,.5)
		scale(.03)
		position(actualVirtualRight - 60, actualVirtualTop.toDouble() + 35)
	}

	val garbageBag = image(resourcesVfs["garbage_bag_one.png"].readBitmap()) {
		anchor(.5,.5)
		scale(.1)
		position(actualVirtualRight - 60, actualVirtualBottom - 60)
	}

	val surfer = sprite(idleAnimation) {
		anchor(.5, .5)
		scale(.9)
		position(actualVirtualRight / 2, virtualBottom - 60)
	}
	surfer.playAnimationLooped(spriteDisplayTime = 200.milliseconds)

	val jellySchool = Array<Sprite>(1) {
		sprite(jellyOneAnimation) {
			anchor(.5, .5)
			scale(.4)
			visible = false
			this.playAnimationLooped(spriteDisplayTime = 90.milliseconds)

		}
	}

	val greenJellySchool = Array<Sprite>(1) {
		sprite(jellyTwoAnimation) {
			anchor(.5, .5)
			scale(.4)
			visible = false
			this.playAnimationLooped(spriteDisplayTime = 90.milliseconds)

		}
	}

	val canCluster = Array<Sprite>(1) {
		sprite(canOneAnimation) {
			anchor(.5, .5)
			scale(.2)
			visible = false
			this.playAnimationLooped(spriteDisplayTime = 90.milliseconds)

		}
	}

	// Level Functions

	fun levelComplete() {
		val gameOver = text("Level Completed") {
			position(centerOnStage())
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


		// WIN Parameters
		if (garbageBag.scale >= .18) {
			levelComplete()
		}


		awaitAll(async {
			jellySchool.forEach {
				if(!it.visible || it.pos.y > height) {
					delay((Random.nextInt(1, 3)).seconds)
					val jellyX = Random.nextInt(buffer, (width.toInt() - buffer)).toDouble()
					it.visible = true
					it.position(jellyX, -5.0)
					it.moveTo(jellyX + 75, 400.0, 1.seconds, Easing.EASE_IN )
					it.moveTo(jellyX + 3, height - buffer, 1.seconds, Easing.EASE_IN)
					it.moveTo(jellyX + 30, height + buffer, 1.seconds, Easing.EASE_IN )
				}
			}
		}, async {
			canCluster.forEach {
				if(!it.visible || it.pos.y > height) {
					delay((Random.nextInt(1, 3)).seconds)
					val canX = Random.nextInt(buffer, (width.toInt() - buffer)).toDouble()
					it.visible = true
					it.position(canX, -5.0)
					it.moveTo(canX, height + buffer, 3.seconds, Easing.EASE_IN)

					it.addUpdater {
						if (surfer.collidesWith(this)) {
							this.visible = false
							garbageBag.scale += .0003
							println("Garbage scale is ${garbageBag.scale}")
						}
					}
				}
			}
		}, async {
			greenJellySchool.forEach {
				if (!it.visible || it.pos.y > height) {
					delay((Random.nextInt(1, 3)).seconds)
					val jellyX = Random.nextInt(buffer, (width.toInt() - buffer)).toDouble()
					it.visible = true
					it.position(jellyX, -5.0)
					it.moveTo(jellyX - 50, 400.0, 2.seconds, Easing.EASE_IN)
					it.moveTo(jellyX + 15, height - buffer, 1.seconds, Easing.EASE_IN)
					it.moveTo(jellyX + 30, height + buffer, 1.seconds, Easing.EASE_IN)
				}
			}
		})

	}



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