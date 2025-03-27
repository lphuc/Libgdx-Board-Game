package com.davik.baseboard.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.davik.baseboard.game.components.ItemAttrComp
import com.davik.baseboard.game.components.itemAttrCmp
import com.davik.baseboard.game.components.removeFromEngine
import com.davik.baseboard.game.components.textTureCmp
import com.davik.baseboard.game.dragndrop.EscSlot
import com.davik.baseboard.game.dragndrop.SlotActor
import com.davik.baseboard.game.dragndrop.SLOT_WIDTH
import com.davik.baseboard.helpers.AttrType
import com.davik.baseboard.helpers.Attribute
import com.davik.baseboard.helpers.DropItem
import com.davik.baseboard.helpers.IItemType
import com.davik.baseboard.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.baseboard.helpers.VIEWPORT_GUI_WIDTH
import com.davik.baseboard.helpers.findIngredientById
import com.davik.baseboard.helpers.sizeAbleBitmap
import com.davik.baseboard.helpers.sizeAbleLblStyleSmall
import com.davik.baseboard.helpers.slotItem
import com.davik.baseboard.helpers.Vector1
import com.davik.baseboard.helpers.getActorScreenPos
import ktx.ashley.get

class BoardWindow(val gameScreen: GameScreen) : Window("", AssLoader.INST().guiSkin) {
    private val SLOT_PER_ROW = 6
    private val chestTable = Table()
    private var characterInfoTbl = Table()
    private val dragAndDrop: DragAndDrop = DragAndDrop()
    var slots = mutableListOf<EscSlot>()
    var slotActors = mutableListOf<SlotActor>()
    var selectedActor: SlotActor? = null

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
            escSlotActor.dropped = false
            escSlotActor.addSpineAnimation()
        }
    }

    fun checkIfMatch(slotActor: SlotActor) {
        if (selectedActor?.slot?.slotItem != null) {
            if (selectedActor?.slot?.slotItem?.itemAttrCmp?.itemType == slotActor.slot.slotItem?.itemAttrCmp?.itemType) {
                slotActor.slot.slotItem?.let {
                    val slotPosition = getActorScreenPos(slotActor)
                    setConsumeItem(slotActor, slotPosition)
                }
                selectedActor?.slot?.slotItem?.let {
                    val slotPosition = getActorScreenPos(selectedActor)
                    setConsumeItem(selectedActor, slotPosition)
                }
                gameScreen.boardGame.audioManager.playGUISound(AssLoader.INST().openBagSound, 1f)
            }
        }
    }

    private fun setConsumeItem(slotActor: SlotActor?, slotPosition: Vector2) {
        if (slotActor == null || slotActor.slot.slotItem?.get(ItemAttrComp.mapper) == null) return
        val slotItem = slotActor.slot.slotItem ?: return
        gameScreen.droppingItems.add(DropItem(
            Image(slotItem.textTureCmp.region).apply {
                setPosition(slotPosition.x, slotPosition.y)
            },
            slotActor,
            Vector1(1f),
            Vector1(SLOT_WIDTH),
            Vector1(slotPosition.y),
            slotPosition.y,
        ))
        slotActor.addAction(Actions.removeActor())
        slotActor.slot.clearListeners()
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

            itemType.let { type ->
                val slot = EscSlot(type)
                slot.slotNumber = index
                slot.slotItem = gameScreen.engine.slotItem("${type}_${{ System.currentTimeMillis().toString().takeLast(5) }}",
                    index,
                    textureId,
                    type,
                    attributes)
                slots.add(slot)
                val slotActor = SlotActor(slot, this, sizeAbleBitmap())
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

    private fun GameScreen.updateDropItems(delta: Float) {
        droppingItems = droppingItems.filter { !it.slotActor.dropped }.toMutableList()
        droppingItems.forEach { dropItem ->
            dropItem.alpha.lerp(Vector1(0f), 4f * delta)
            dropItem.yPos.interpolate(Vector1(dropItem.originY - SLOT_WIDTH * 6f), 0.1f, Interpolation.slowFast)
            dropItem.size.lerp(SLOT_WIDTH, delta)
            dropItem.itemImg.width = dropItem.size.value
            dropItem.itemImg.height = dropItem.size.value
            dropItem.itemImg.setPosition(dropItem.itemImg.x, dropItem.yPos.value)

            if (dropItem.yPos.value <= dropItem.originY - SLOT_WIDTH * 5f && !dropItem.slotActor.dropped) {
                dropItem.slotActor.dropped = true
                dropItem.slotActor.slot.slotItem?.removeFromEngine(engine)
                selectedActor = null
            }
        }
    }

    fun update(delta: Float) {
        gameScreen.updateDropItems(delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        //draw moving item separately with chestWindow (to use alpha)

        gameScreen.droppingItems.forEach { dropItem ->
            dropItem.itemImg.act(Gdx.graphics.deltaTime)
            dropItem.itemImg.draw(batch, dropItem.alpha.value)
        }
    }
}
