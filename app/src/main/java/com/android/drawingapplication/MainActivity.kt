package com.android.drawingapplication

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.android.drawingapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var mimage_button_current_paint: ImageButton? = null
    private var mCurrentTool: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCurrentTool = rel_layout[0] as ImageButton
//        mCurrentTool!!.setImageDrawable(
//            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
//        )

        ll_paint_colors.visibility = View.INVISIBLE
        mimage_button_current_paint = ll_paint_colors[3] as ImageButton
        mimage_button_current_paint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )

        canvasbit.setBrushSize(8.toFloat())
        ib_brush.setOnClickListener()
        {
            show_dialog_brush_size()
        }

        palette.setOnClickListener() {
            ll_paint_colors.visibility = View.VISIBLE
        }

        rect.setOnClickListener() {
            canvasbit.setLine(false)
            canvasbit.setRectangle(true)
            canvasbit.setCircle(false)
            canvasbit.setArrow(false)
        }

        arrow.setOnClickListener() {
            canvasbit.setArrow(true)
            canvasbit.setLine(false)
            canvasbit.setRectangle(false)
            canvasbit.setCircle(false)
        }

        pencil.setOnClickListener() {
            canvasbit.setLine(true)
            canvasbit.setArrow(false)
            canvasbit.setRectangle(false)
            canvasbit.setCircle(false)
        }

        ellipse.setOnClickListener() {
            canvasbit.setLine(false)
            canvasbit.setRectangle(false)
            canvasbit.setCircle(true)
            canvasbit.setArrow(false)
        }

        eraser.setOnClickListener() {
            canvasbit.onEraseLast()
        }
    }

    private fun show_dialog_brush_size() {
        val brushdialog = Dialog(this)
        brushdialog.setContentView(R.layout.dialog_brush_size)
        brushdialog.setTitle("BRUSH SIZE")
        var small = brushdialog.small
        var medium = brushdialog.medium
        var large = brushdialog.large

        small.setOnClickListener()
        {
            canvasbit.setBrushSize(8.toFloat())
            brushdialog.dismiss()
        }

        medium.setOnClickListener()
        {
            canvasbit.setBrushSize(12.toFloat())
            brushdialog.dismiss()
        }

        large.setOnClickListener()
        {
            canvasbit.setBrushSize(16.toFloat())
            brushdialog.dismiss()
        }
        brushdialog.show()
    }

//    private fun show_dialog_color() {
//        val colordialog = Dialog(this)
//        colordialog.setContentView(R.layout.dialog_brush_color)
//        colordialog.setTitle("BRUSH COLOR")
//        var red = colordialog.red
//        var green = colordialog.green
//        var blue = colordialog.blue
//        var black = colordialog.black
//
//        red.setOnClickListener() {
//            canvasbit.setcolornew("#FF0000")
//        }
//
//        green.setOnClickListener() {
//            canvasbit.setcolornew("#FF0000")
//        }
//
//        blue.setOnClickListener() {
//            canvasbit.setcolornew("#FF0000")
//        }
//
//        black.setOnClickListener() {
//            canvasbit.setcolornew("#FF0000")
//        }
//    }

    fun paintclicked(view: View) {
        if (view != mimage_button_current_paint) {
            var imagebuttonpaint = view as ImageButton
            var newcolor = imagebuttonpaint.tag.toString()
            canvasbit.setcolornew(newcolor)
            imagebuttonpaint.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
            )

            mimage_button_current_paint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )
            mimage_button_current_paint = view

            ll_paint_colors.visibility = View.INVISIBLE
        }

    }
}