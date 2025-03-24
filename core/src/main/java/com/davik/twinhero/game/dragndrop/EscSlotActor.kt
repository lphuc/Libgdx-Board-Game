package com.davik.twinhero.game.dragndrop

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.davik.twinhero.game.AssLoader
import com.davik.twinhero.game.BoardWindow
import com.davik.twinhero.game.SpineActor
import com.davik.twinhero.game.components.textTureCmp
import com.davik.twinhero.helpers.CenterImageTextButton
import com.davik.twinhero.helpers.SpineSlotCfg
import com.davik.twinhero.helpers.getActorScreenPos
import com.davik.twinhero.helpers.sizeAbleBitmap
import com.esotericsoftware.spine.AnimationState
import com.esotericsoftware.spine.Event

class EscSlotActor(slot: EscSlot, val boardWindow: BoardWindow, bitmapFont: BitmapFont? = null) : Stack(), EscSlotListener {
    var slot: EscSlot
    var faceOff = false
    private val skin: Skin
    private val baseImageButton: CenterImageTextButton = CenterImageTextButton("", AssLoader.INST().guiSkin)
    private var spineActor: SpineActor? = null
    var isDragging = false
    private var sinceAddToBoard = 0L
    var isSelected = false

    init {
        val bitmap = bitmapFont ?: sizeAbleBitmap()
        bitmap.let {
            val style = createStyle(AssLoader.INST().guiSkin, "no_bg", slot, it)
            baseImageButton.style = style
        }

        if (slot.amount > 1) {
            baseImageButton.text = slot.amount.toString()
        }

        this.slot = slot
        this.skin = AssLoader.INST().guiSkin

        slot.removeSlotListener(this)
        slot.addSlotListener(this)

        addListener(object : com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                isSelected = !isSelected

                if (isSelected) {
                    spineActor?.skeleton?.setAttachment("slot_bg", "slot_bg_select")
                } else {
                    spineActor?.skeleton?.setAttachment("slot_bg",  "slot_bg_default")
                }
            }

        })

        add(baseImageButton)  // should be replaced by spine image
    }

    fun addSpineAnimation() {
        sinceAddToBoard = System.currentTimeMillis()
        spineActor = SpineActor(SpineSlotCfg.slotSkeletonData, SpineSlotCfg.slotAnimData)
        spineActor?.animationState?.setAnimation(0, "slot_idle", true)?.apply {
            timeScale = MathUtils.random(0.2f, 0.25f)
        }
        spineActor?.animationState?.addListener(object : AnimationState.AnimationStateAdapter() {
            override fun event(entry: AnimationState.TrackEntry?, event: Event?) {
                super.event(entry, event)
                if (event?.data?.name.equals("flip_transition")) {
                    spineActor?.skeleton?.setAttachment("slot", "paper")
                }
            }

            override fun complete(entry: AnimationState.TrackEntry?) {
                super.complete(entry)
                if (entry?.animation?.name == "flip") {
                    spineActor?.animationState?.setAnimation(0, "slot_idle", true)?.apply {
                        timeScale = MathUtils.random(0.2f, 0.25f)
                    }
                }
            }
        })

        spineActor?.skeleton?.setScale(SLOT_WIDTH / 340f, SLOT_WIDTH / 340f)
        val screenPosition = getActorScreenPos(this)
        spineActor?.position = Vector2(screenPosition.x + SLOT_WIDTH / 4f, screenPosition.y)
        if (slot.isHighlight && slot.slotItem != null) {
            spineActor?.skeleton?.setAttachment("slot", "paper_highlight")
        } else if (slot.slotItem != null) {
            spineActor?.skeleton?.setAttachment("slot", slot.slotItem?.textTureCmp?.textureId)
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        spineActor?.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        spineActor?.draw(batch, parentAlpha)
    }

    override fun onSlotChanged(slot: EscSlot, isDrag: Boolean) {
        baseImageButton.style = createStyle(skin, "no_bg", slot, sizeAbleBitmap())

        // baseImageButton.style.up = null
        // baseImageButton.style.down = TextureRegionDrawable(AssLoader.INST().guiImageAtlas.findRegion("bg_shop_item1"))
        // baseImageButton.style.over = null

        //add "fly into slot" animation (don't use for empty slot)
        if (slot.slotItem != null && isDrag) {
            val originWidth = if (baseImageButton.stack.prefWidth > SLOT_WIDTH) {
                SLOT_WIDTH
            } else {
                baseImageButton.stack.prefWidth
            }

            val originHeight = if (baseImageButton.stack.prefWidth > SLOT_WIDTH) {
                SLOT_WIDTH
            } else {
                baseImageButton.stack.prefHeight
            }

            baseImageButton.stack.width = if (isDrag) {
                if (baseImageButton.stack.prefWidth > originWidth) {
                    baseImageButton.stack.prefWidth
                } else {
                    originWidth * 1.2f
                }
            } else {
                originWidth / 0.8f
            }
            baseImageButton.stack.height = if (isDrag) {
                if (baseImageButton.stack.prefHeight > originHeight) {
                    baseImageButton.stack.prefHeight
                } else {
                    originWidth * 1.2f
                }
            } else {
                originHeight / 0.8f
            }

            baseImageButton.stack.addAction(Actions.sizeTo(originWidth, originHeight, 0.18f))
        }
    }

    private fun createStyle(skin: Skin, styleName: String, slot: EscSlot, font: BitmapFont): ImageTextButton.ImageTextButtonStyle {
        val style = ImageTextButton.ImageTextButtonStyle(skin.get(styleName, ImageTextButton.ImageTextButtonStyle::class.java))
        style.font = font
        if (slot.slotItem == null) {
            style.imageUp = TextureRegionDrawable(AssLoader.INST().guiImageAtlas.findRegion("invaded_slot"))
            style.imageDown = TextureRegionDrawable(AssLoader.INST().guiImageAtlas.findRegion("invaded_slot"))
            style.imageOver = TextureRegionDrawable(AssLoader.INST().guiImageAtlas.findRegion("invaded_slot"))
        }

        if (style.imageUp != null) {
            style.imageUp.minWidth = SLOT_WIDTH
            style.imageUp.minHeight = SLOT_WIDTH
            style.imageDown.minWidth = SLOT_WIDTH
            style.imageDown.minHeight = SLOT_WIDTH
            style.imageOver.minWidth = SLOT_WIDTH
            style.imageOver.minHeight = SLOT_WIDTH
        }

        return style
    }

    interface SelectListener {
        fun onSelect(slot: EscSlot)
    }
}
