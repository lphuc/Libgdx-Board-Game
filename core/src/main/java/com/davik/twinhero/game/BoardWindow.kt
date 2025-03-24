package com.davik.twinhero.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.davik.twinhero.game.components.ItemAttrComp
import com.davik.twinhero.game.components.TextureComp
import com.davik.twinhero.game.components.itemAttrCmp
import com.davik.twinhero.game.components.removeFromEngine
import com.davik.twinhero.game.components.textTureCmp
import com.davik.twinhero.game.dragndrop.EscSlot
import com.davik.twinhero.game.dragndrop.EscSlotActor
import com.davik.twinhero.game.dragndrop.EscSlotSource
import com.davik.twinhero.game.dragndrop.EscSlotTarget
import com.davik.twinhero.game.dragndrop.SLOT_WIDTH
import com.davik.twinhero.helpers.AttrType
import com.davik.twinhero.helpers.Attribute
import com.davik.twinhero.helpers.ItemType
import com.davik.twinhero.helpers.IItemType
import com.davik.twinhero.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.twinhero.helpers.VIEWPORT_GUI_WIDTH
import com.davik.twinhero.helpers.findIngredientById
import com.davik.twinhero.helpers.safeDiv
import com.davik.twinhero.helpers.sizeAbleBitmap
import com.davik.twinhero.helpers.sizeAbleLblStyleSmall
import com.davik.twinhero.helpers.slotItem
import com.davik.twinhero.helpers.MovingActor
import com.davik.twinhero.helpers.SpecialType
import com.davik.twinhero.helpers.getAttributeColor
import com.davik.twinhero.helpers.updateDroppingActors
import com.davik.twinhero.helpers.updateFlyingItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.ashley.get
import ktx.async.KtxAsync

class BoardWindow(val gameScreen: GameScreen) : Window("", AssLoader.INST().guiSkin) {
    private val SLOT_PER_ROW = 6
    private val chestTable = Table()
    private var characterInfoTbl = Table()
    private val dragAndDrop: DragAndDrop = DragAndDrop()
    var slots = mutableListOf<EscSlot>()
    var slotActors = mutableListOf<EscSlotActor>()
    var toDropSlotActors = mutableListOf<MovingActor>()

    var timeLbl: Label = Label("00:00", sizeAbleLblStyleSmall()).apply {
        setPosition(VIEWPORT_GUI_WIDTH / 2f - prefWidth / 2f, VIEWPORT_GUI_HEIGHT - prefHeight * 2f)
    }

    init {
        this.addActor(timeLbl)
        chestTable.defaults().space(8f)
        generateRandomSlot()
        chestTable.center()
        this.add(chestTable).width(VIEWPORT_GUI_HEIGHT / 1.1f).height(VIEWPORT_GUI_HEIGHT / 1.1f)
        this.row()
        this.add(characterInfoTbl).width(VIEWPORT_GUI_HEIGHT / 4f).height(SLOT_WIDTH).padBottom(SLOT_WIDTH / 4f)
        center()
        pack()
        this.setKeepWithinStage(false)
        this.height = VIEWPORT_GUI_HEIGHT / 1.07f
        this.width = VIEWPORT_GUI_WIDTH / 1.05f
        this.isMovable = false
        this.isVisible = false
        this.validate()
    }

    fun updateSlotSpinePosition() {
        slotActors.forEach { escSlotActor ->
            escSlotActor.faceOff = false
            escSlotActor.addSpineAnimation()
        }
    }

    fun resetBoardTable() {
        slots.forEach { slot ->
            slot.slotItem?.removeFromEngine(gameScreen.engine)
        }
        slots.clear()
        chestTable.clear()
        slotActors.forEach { actor ->
            actor.addAction(Actions.removeActor())
        }
        generateRandomSlot()
        updateSlotSpinePosition()
    }

    private fun generateRandomSlot() {
        for (index in 1..60) {
            var itemType: IItemType? = null
            val attributes = mutableListOf<Attribute>()
            var textureId = ""
            val randItem = MathUtils.random(1, 16)
            itemType = findIngredientById(randItem)
            textureId = "food$randItem"
            val attribute = Attribute(AttrType.PLUS_TIME, randItem + MathUtils.random(1F, 5F))
            attributes.add(attribute)

            itemType.let {
                val slot = EscSlot(it)
                slot.slotNumber = index
                slot.slotItem = gameScreen.engine.slotItem("${it}_${{ System.currentTimeMillis().toString().takeLast(5) }}",
                    index,
                    textureId,
                    it,
                    attributes)
                slots.add(slot)
                val slotActor = EscSlotActor(slot, this, sizeAbleBitmap())
                slotActors.add(slotActor)
                //dragAndDrop.addSource(EscSlotSource(slotActor))
                //dragAndDrop.addTarget(EscSlotTarget(slotActor))
                chestTable.add<Actor>(slotActor).size(SLOT_WIDTH, SLOT_WIDTH).pad(SLOT_WIDTH / 16f)
                if (index % SLOT_PER_ROW == 0) {
                    chestTable.row()
                }
                // chestTable.debugCell()
            }
        }
    }

    fun setConsumeItem(slotItem: Entity, slotNumber: Int, slotPosition: Vector2) {

        //TODO: toDropSlotActors.add(MovingActor(slotActor, slotActor.x, slotActor.y))

        if (slotItem[ItemAttrComp.mapper] == null || slotItem[TextureComp.mapper] == null) return

        gameScreen.movingItemImg = Image(slotItem.textTureCmp.region).apply {
            setPosition(slotPosition.x, slotPosition.y)
        }
        gameScreen.movingItemPos = slotPosition
        gameScreen.originYPos = slotPosition.y
        gameScreen.movingItem = slotItem

        gameScreen.twinHero.audioManager.playGUISound(AssLoader.INST().openBagSound, 1f)

        KtxAsync.launch {
            delay(1000)
            if (gameScreen.movingItem?.itemAttrCmp?.itemType is ItemType) {
                gameScreen.twinHero.audioManager.playGUISound(AssLoader.INST().itemSpawnSound, 1f)
            } else if (slotItem.itemAttrCmp.itemType is SpecialType) {
                gameScreen.twinHero.audioManager.playGUISound(AssLoader.INST().equipItemSound, 1f)
            }
        }
    }

    fun update(delta: Float) {
        gameScreen.updateFlyingItem(delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        //draw moving item separately with chestWindow (to use alpha)
        gameScreen.movingItemImg?.act(Gdx.graphics.deltaTime)
        gameScreen.movingItemImg?.draw(batch, gameScreen.movingItemAlpha.value)
        updateDroppingActors()
    }
}
