package com.android.drawingapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mPathDraw: Custompath? = null
    private var mCanvasBitMap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var Canvas: Canvas? = null
    private val mpath = ArrayList<Custompath>()
    private val mrectangle = ArrayList<Rectangle>()
    private val mCircle = ArrayList<Circle>()
    private val mArrow = ArrayList<Arrow>()
    private var rectangle: Boolean = false
    private var currRectangle: Rectangle? = null
    private var circle: Boolean = false
    private var currCircle: Circle? = null
    private var arrow: Boolean = false
    private var currArrow: Arrow? = null
    private var line: Boolean = false
    private var startX: Float? = 0f
    private var startY: Float? = 0f
    private var endX: Float? = 0f
    private var endY: Float? = 0f
//    private val mEraseLast = ArrayList<All>()
//    private val erase = ArrayList<All>()

    init {
        setupdrawing()
    }

    fun onEraseLast() {
        mpath.clear()
        mArrow.clear()
        mrectangle.clear()
        mCircle.clear()

        invalidate()
    }

    private fun setupdrawing() {
        mDrawPaint = Paint()
        mPathDraw = Custompath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
//        mBrushSize= 20.toFloat()

    }

    // Change of size of canvas map accoering to the device screen size
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        Canvas = Canvas(mCanvasBitMap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (path in mpath) {
            mDrawPaint!!.strokeWidth = path.brushthickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        canvas.drawBitmap(mCanvasBitMap!!, 0f, 0f, mCanvasPaint)
        if (!mPathDraw!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mPathDraw!!.brushthickness
            mDrawPaint!!.color = mPathDraw!!.color
            canvas.drawPath(mPathDraw!!, mDrawPaint!!)
        }

        mrectangle.forEach {
            mDrawPaint!!.strokeWidth = it.paint.brushthickness
            mDrawPaint!!.color = it.paint.color
            canvas.drawRect(it.startX!!, it.startY!!, it.endX!!, it.endY!!, mDrawPaint!!)
        }
        currRectangle?.let {
            mDrawPaint!!.strokeWidth = it.paint.brushthickness
            mDrawPaint!!.color = it.paint.color
            canvas.drawRect(it.startX!!, it.startY!!, it.endX!!, it.endY!!, mDrawPaint!!)
        }

        mCircle.forEach {
            mDrawPaint!!.strokeWidth = it.paint.brushthickness
            mDrawPaint!!.color = it.paint.color
            canvas.drawCircle(it.centerX!!, it.centerY!!, it.radius!!, mDrawPaint!!)
        }

        currCircle?.let {
            mDrawPaint!!.strokeWidth = it.paint.brushthickness
            mDrawPaint!!.color = it.paint.color
            canvas.drawCircle(it.centerX!!, it.centerY!!, it.radius!!, mDrawPaint!!)
        }

        mArrow.forEach {
            mDrawPaint!!.strokeWidth = it.paint.brushthickness
            mDrawPaint!!.color = it.paint.color
            canvas.drawLine(it.startX!!, it.startY!!, it.endX!!, it.endY!!, mDrawPaint!!)
        }
        currArrow?.let {
            mDrawPaint!!.strokeWidth = it.paint.brushthickness
            mDrawPaint!!.color = it.paint.color
            canvas.drawLine(it.startX!!, it.startY!!, it.endX!!, it.endY!!, mDrawPaint!!)
        }

//        mEraseLast.forEach {
//           if (it.allRect != null) {
//               mDrawPaint!!.strokeWidth = it.allRect.paint.brushthickness
//               mDrawPaint!!.color = it.allRect.paint.color
//               canvas.drawRect(it.allRect.startX!!, it.allRect.startY!!, it.allRect.endX!!, it.allRect.endY!!, mDrawPaint!!)
//               }
//
//        }

    }

    // The Event in which when we give input
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchx = event?.x
        val touchy = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mPathDraw!!.color = color
                mPathDraw!!.brushthickness = mBrushSize
                mPathDraw!!.reset()
                if (touchx != null)
                    if (touchy != null) {
                        startX = touchx
                        startY = touchy
                        if (circle) {
                            currCircle = Circle(touchx, touchy, 0f, Custompath(color, mBrushSize))
                        }
                        if (line) {
                            mPathDraw!!.moveTo(touchx, touchy)
                        }
                        if (rectangle) {
                            currRectangle = Rectangle(startX, startY, startX, startY, Custompath(color, mBrushSize))
                        }
                        if(arrow) {
                            currArrow = Arrow(startX, startY, startX, startY, Custompath(color, mBrushSize))
                        }
                    }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchx != null) {
                    if (touchy != null) {
                        if (line) {
                            mPathDraw!!.lineTo(touchx, touchy)
                        }
                        if (rectangle) {
                            currRectangle = currRectangle?.copy(
                                startX = startX,
                                startY = startY,
                                endX = touchx,
                                endY = touchy
                            )
                        }
                        if (circle){
                            val distance: Float = sqrt((touchx - startX!!).toDouble().pow(2.0) + (touchy - startY!!).toDouble().pow(2.0)).toFloat()
                            val center = distance/2
                            currCircle = Circle(startX!! + center, startY!! + center, distance/2, Custompath(color, mBrushSize))
                        }
                        if (arrow) {
                            currArrow = currArrow?.copy(
                                startX = startX,
                                startY = startY,
                                endX = touchx,
                                endY = touchy
                            )
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                endX = touchx
                endY = touchy
                if (circle){
                    currCircle = null
                    val distance = sqrt((endX!! - startX!!).toDouble().pow(2.0) + (endY!! - startY!!).toDouble().pow(2.0)).toFloat()
                    val center = distance/2
                    mCircle.add(Circle(startX!! + center, startY!! + center, center, Custompath(color, mBrushSize)))
//                    mEraseLast.add(All(null, null, null, Circle(startX!! + center, startY!! + center, center, Custompath(color, mBrushSize))))
                }
                if (rectangle) {
                    currRectangle = null
                    mrectangle.add(Rectangle(startX, startY, endX, endY, Custompath(color, mBrushSize)))
//                    mEraseLast.add(All(null, null, Rectangle(startX, startY, endX, endY, Custompath(color, mBrushSize)), null))
                }
                if(arrow) {
                    currArrow = null
                    mArrow.add(Arrow(startX, startY, endX, endY, Custompath(color, mBrushSize)))
//                    mEraseLast.add(All(null, Arrow(startX, startY, endX, endY, Custompath(color, mBrushSize)), null, null))
                }
                if (line) {
                    mpath.add(mPathDraw!!)
//                    mEraseLast.add(All(mPathDraw!!, null, null, null))
                }
                mPathDraw = Custompath(color, mBrushSize)

            }
            else -> return false
        }

        invalidate()
        return true
    }

    fun setBrushSize(newSize: Float) {
        mBrushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize,
            resources.displayMetrics
        )
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    fun setcolornew(newColor: String) {
        color = Color.parseColor(newColor)
        mPathDraw!!.color = color
    }

    fun setRectangle(set: Boolean) {
        rectangle = set
    }

    fun setCircle(set: Boolean) {
        circle = set
    }

    fun setLine(set: Boolean) {
        line = set
    }

    fun setArrow(set: Boolean) {
        arrow = set
    }

    data class Rectangle(val startX: Float?, val startY: Float?, val endX: Float?, val endY: Float?, val paint: Custompath)

    data class Circle(val centerX: Float?, val centerY: Float?, val radius: Float?, val paint: Custompath)

    data class Arrow(val startX: Float?, val startY: Float?, val endX: Float?, val endY: Float?, val paint: Custompath)

    inner class Custompath(var color: Int, var brushthickness: Float) : Path() {

    }

//    data class All(val allLine: Custompath?, val allArrow: Arrow?, val allRect: Rectangle?, val allCircle: Circle?)

}